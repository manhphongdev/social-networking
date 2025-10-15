// api.ts
import axios, {AxiosError} from "axios"

const API = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8081",
    withCredentials: true,
})

// ==================
// Type chung cho API response
// ==================
interface ApiResponse<T> {
    status: number
    message: string
    data: T
}

interface RefreshResponse {
    accessToken: string
}

// ==================
// Quản lý queue refresh
// ==================
let isRefreshing = false
let failedQueue: {
    resolve: (token: string | null) => void
    reject: (err: any) => void
}[] = []

function processQueue(error: any, token: string | null = null) {
    failedQueue.forEach((prom) => {
        if (error) prom.reject(error)
        else prom.resolve(token)
    })
    failedQueue = []
}

// ==================
// Request interceptor: gắn accessToken
// ==================
API.interceptors.request.use((config) => {
    if (typeof window !== "undefined") {
        const token = localStorage.getItem("accessToken")
        if (token) {
            config.headers = config.headers || {}
            config.headers.Authorization = `Bearer ${token}`
        }
    }
    return config
})


// ==================
// Response interceptor: xử lý khi token hết hạn
// ==================
API.interceptors.response.use(
    (res) => res,
    async (error: AxiosError) => {
        const originalRequest: any = error.config

        if (
            (error.response?.status === 401 || error.response?.status === 403) &&
            !originalRequest._retry
        ) {
            if (isRefreshing) {
                return new Promise((resolve, reject) => {
                    failedQueue.push({resolve, reject})
                }).then((token) => {
                    if (token) {
                        originalRequest.headers.Authorization = `Bearer ${token}`
                    }
                    return API(originalRequest)
                })
            }

            originalRequest._retry = true
            isRefreshing = true

            try {
                const res = await API.post<ApiResponse<RefreshResponse>>(
                    "/auth/refresh-token",
                    {},
                    {withCredentials: true}
                )

                const newToken = res.data.data.accessToken
                localStorage.setItem("accessToken", newToken)

                processQueue(null, newToken)

                originalRequest.headers.Authorization = `Bearer ${newToken}`
                return API(originalRequest)
            } catch (err) {
                processQueue(err, null)
                localStorage.removeItem("accessToken")
                return Promise.reject(err)
            } finally {
                isRefreshing = false
            }
        }

        return Promise.reject(error)
    }
)

export default API

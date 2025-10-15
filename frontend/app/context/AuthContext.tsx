"use client"

import {createContext, useContext, useEffect, useState} from "react"

type AuthContextType = {
    isAuthenticated: boolean
    avatarUrl: string | null
    login: (token: string, avatar?: string) => void
    logout: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({children}: { children: React.ReactNode }) {
    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const [avatarUrl, setAvatarUrl] = useState<string | null>(null)

    useEffect(() => {
        const token = localStorage.getItem("accessToken")
        const avatar = localStorage.getItem("avatar")
        setIsAuthenticated(!!token)
        setAvatarUrl(avatar)
    }, [])

    const login = (token: string, avatar?: string) => {
        localStorage.setItem("accessToken", token)
        if (avatar) localStorage.setItem("avatar", avatar)
        setIsAuthenticated(true)
        setAvatarUrl(avatar || null)
    }

    const logout = () => {
        localStorage.removeItem("accessToken")
        localStorage.removeItem("avatar")
        localStorage.removeItem("userId")
        setIsAuthenticated(false)
        setAvatarUrl(null)
    }

    return (
        <AuthContext.Provider value={{isAuthenticated, avatarUrl, login, logout}}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    const ctx = useContext(AuthContext)
    if (!ctx) throw new Error("useAuth must be used within AuthProvider")
    return ctx
}

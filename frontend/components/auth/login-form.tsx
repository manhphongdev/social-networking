"use client"

import React, {useState} from "react"
import {useRouter} from "next/navigation"
import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {Eye, EyeOff, Lock, Mail, Shield, User} from "lucide-react"
import {toast} from "sonner"
import {useAuth} from "@/app/context/AuthContext";

export function LoginForm() {
    const [showPassword, setShowPassword] = useState(false)
    const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL
    const [formData, setFormData] = useState({
        email: "",
        password: "",
    })
    const router = useRouter()
    const {login} = useAuth()

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        console.log("Login attempt:", formData)

        try {
            const res = await fetch(API_BASE_URL + "/auth/log-in", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(formData),
            })
            const data = await res.json()

            if (!res.ok) {
                if (data.status === 404 || data.status === 401) {
                    toast.error("Invalid username or password")
                } else if (res.status === 403) {
                    toast.error("Access denied")

                } else if (data.status === 400) {
                    toast.error(data.message)
                } else {
                    toast.error("Login failed. Please try again.")
                }
                return
            }

            if (data.data.user.status === "INACTIVE") {
                toast.error("Please verify your email before logging in.")
                return
            }

            if (data.data.token?.accessToken) {
                login(data.data.token.accessToken, data.data.user.avatar)
                localStorage.setItem("userId", data.data.user.id)
                if (data.data.user.roleName === "ADMIN") {
                    router.push("/admin")
                } else {
                    router.push("/feed")
                }
                toast.success(`Welcome back, ${data.data.user.name}`)
            }
        } catch (er) {
            console.log(er)
            toast.error("Something went wrong. Please try again.")
        }
    }

    const fillTestCredentials = (type: "user" | "admin") => {
        if (type === "user") {
            setFormData({
                email: "user@test.com",
                password: "12345678",
            })
        } else {
            setFormData({
                email: "admin@test.com",
                password: "12345678",
            })
        }
    }

    return (
        <Card className="w-full max-w-md mx-auto">
            <CardHeader className="space-y-1">
                <CardTitle className="text-2xl font-bold text-center">
                    Đăng nhập
                </CardTitle>
                <CardDescription className="text-center">
                    Nhập thông tin để truy cập tài khoản của bạn
                </CardDescription>
            </CardHeader>
            <CardContent>
                {/* Tài khoản test */}
                <div className="mb-6 p-4 bg-muted/50 rounded-lg border">
                    <h3 className="text-sm font-medium mb-3 text-center">Tài khoản test</h3>
                    <div className="space-y-2">
                        <Button
                            type="button"
                            variant="outline"
                            size="sm"
                            className="w-full justify-start bg-transparent"
                            onClick={() => fillTestCredentials("user")}
                        >
                            <User className="h-4 w-4 mr-2"/>
                            Người dùng: user@test.com / 12345678
                        </Button>
                        <Button
                            type="button"
                            variant="outline"
                            size="sm"
                            className="w-full justify-start bg-transparent"
                            onClick={() => fillTestCredentials("admin")}
                        >
                            <Shield className="h-4 w-4 mr-2"/>
                            Quản trị: admin@test.com / 12345678
                        </Button>
                    </div>
                </div>

                {/* Form login */}
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div className="space-y-2">
                        <Label htmlFor="email">Email</Label>
                        <div className="relative">
                            <Mail className="absolute left-3 top-3 h-4 w-4 text-muted-foreground"/>
                            <Input
                                id="email"
                                type="email"
                                placeholder="your@email.com"
                                className="pl-10"
                                value={formData.email}
                                onChange={(e) =>
                                    setFormData({...formData, email: e.target.value})
                                }
                                required
                            />
                        </div>
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="password">Mật khẩu</Label>
                        <div className="relative">
                            <Lock className="absolute left-3 top-3 h-4 w-4 text-muted-foreground"/>
                            <Input
                                id="password"
                                type={showPassword ? "text" : "password"}
                                placeholder="••••••••"
                                className="pl-10 pr-10"
                                value={formData.password}
                                onChange={(e) =>
                                    setFormData({...formData, password: e.target.value})
                                }
                                required
                            />
                            <Button
                                type="button"
                                variant="ghost"
                                size="sm"
                                className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent"
                                onClick={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? (
                                    <EyeOff className="h-4 w-4 text-muted-foreground"/>
                                ) : (
                                    <Eye className="h-4 w-4 text-muted-foreground"/>
                                )}
                            </Button>
                        </div>
                    </div>
                    <Button type="submit" className="w-full">
                        Đăng nhập
                    </Button>
                </form>
            </CardContent>
        </Card>
    )
}

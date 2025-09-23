"use client"

import type React from "react"
import { useState } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Eye, EyeOff, Mail, Lock, User, Shield } from "lucide-react"

export function LoginForm() {
  const [showPassword, setShowPassword] = useState(false)
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  })
  const router = useRouter()

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log("Login attempt:", formData)

    // Simple authentication check
    if (formData.email === "admin@test.com" && formData.password === "admin123") {
      // Redirect to admin dashboard
      router.push("/admin")
    } else if (formData.email === "user@test.com" && formData.password === "123456") {
      // Redirect to user feed
      router.push("/feed")
    } else {
      alert("Email hoặc mật khẩu không đúng!")
    }
  }

  const fillTestCredentials = (type: "user" | "admin") => {
    if (type === "user") {
      setFormData({
        email: "user@test.com",
        password: "123456",
      })
    } else {
      setFormData({
        email: "admin@test.com",
        password: "admin123",
      })
    }
  }

  return (
    <Card className="w-full max-w-md mx-auto">
      <CardHeader className="space-y-1">
        <CardTitle className="text-2xl font-bold text-center">Đăng nhập</CardTitle>
        <CardDescription className="text-center">Nhập thông tin để truy cập tài khoản của bạn</CardDescription>
      </CardHeader>
      <CardContent>
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
              <User className="h-4 w-4 mr-2" />
              Người dùng: user@test.com / 123456
            </Button>
            <Button
              type="button"
              variant="outline"
              size="sm"
              className="w-full justify-start bg-transparent"
              onClick={() => fillTestCredentials("admin")}
            >
              <Shield className="h-4 w-4 mr-2" />
              Quản trị: admin@test.com / admin123
            </Button>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="email">Email</Label>
            <div className="relative">
              <Mail className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
              <Input
                id="email"
                type="email"
                placeholder="your@email.com"
                className="pl-10"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                required
              />
            </div>
          </div>
          <div className="space-y-2">
            <Label htmlFor="password">Mật khẩu</Label>
            <div className="relative">
              <Lock className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
              <Input
                id="password"
                type={showPassword ? "text" : "password"}
                placeholder="••••••••"
                className="pl-10 pr-10"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
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
                  <EyeOff className="h-4 w-4 text-muted-foreground" />
                ) : (
                  <Eye className="h-4 w-4 text-muted-foreground" />
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

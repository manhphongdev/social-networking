"use client"

import React, {useState} from "react"
import {useRouter} from "next/navigation"
import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select"
import {Calendar, Eye, EyeOff, Lock, Mail, Upload, User} from "lucide-react"
import {toast} from "sonner"

export function RegisterForm() {
    const [showPassword, setShowPassword] = useState(false)
    const [showConfirmPassword, setShowConfirmPassword] = useState(false)
    const [avatarPreview, setAvatarPreview] = useState<string | null>(null)
    const [isLoading, setIsLoading] = useState(false)
    const [formData, setFormData] = useState({
        email: "",
        password: "",
        confirmPassword: "",
        fullName: "",
        dateOfBirth: "",
        gender: "",
        avatar: null as File | null,
    })
    const router = useRouter()
    const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8001"

    const handleAvatarChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]
        if (file) {
            if (file.size > 5 * 1024 * 1024) { // Giới hạn 5MB
                toast.error("Ảnh đại diện không được vượt quá 5MB")
                return
            }
            setFormData({...formData, avatar: file})
            const reader = new FileReader()
            reader.onload = (e) => {
                setAvatarPreview(e.target?.result as string)
            }
            reader.readAsDataURL(file)
        }
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        if (formData.password !== formData.confirmPassword) {
            toast.error("Mật khẩu xác nhận không khớp")
            return
        }

        const isValidEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)
        if (!isValidEmail) {
            toast.error("Vui lòng nhập email hợp lệ")
            return
        }

        if (formData.password.length < 8) {
            toast.error("Mật khẩu phải có ít nhất 8 ký tự")
            return
        }

        setIsLoading(true)

        try {
            // Tạo multipart FormData để gửi
            const payload = new FormData()

            // Gói user thành JSON blob
            const userPayload = {
                email: formData.email,
                password: formData.password,
                name: formData.fullName,
                dateOfBirth: formData.dateOfBirth,
                gender: formData.gender,
            }
            payload.append(
                "user",
                new Blob([JSON.stringify(userPayload)], {type: "application/json"})
            )

            // Gắn file avatar nếu có
            if (formData.avatar) {
                payload.append("file", formData.avatar)
            }

            const res = await fetch(`${API_BASE_URL}/users/register`, {
                method: "POST",
                body: payload,
            })

            const data = await res.json()

            console.log(data)

            if (data.status !== 201) {
                if (data.status === 400) {
                    toast.error(data.message)
                } else if (data.status === 401 || data.status === 403) {
                    toast.error(data.message)
                } else if (data.status === 1002) {
                    toast.error(data.message)
                } else {
                    toast.error("Đăng ký thất bại. Vui lòng thử lại.")
                }
                return
            }
            toast.success("Đăng ký thành công! Vui lòng kiểm tra email để xác minh.")
            router.push("/auth/login")
        } catch (er) {
            console.error(er)
            toast.error("Đã xảy ra lỗi. Vui lòng thử lại.")
        } finally {
            setIsLoading(false)
        }
    }

    const fillTestCredentials = () => {
        setFormData({
            email: "testuser@test.com",
            password: "12345678",
            confirmPassword: "12345678",
            fullName: "Nguyễn Văn Test",
            dateOfBirth: "1990-01-01",
            gender: "male",
            avatar: null,
        })
        setAvatarPreview(null)
    }

    return (
        <Card className="w-full max-w-md mx-auto">
            <CardHeader className="space-y-1">
                <CardTitle className="text-2xl font-bold text-center">Đăng ký</CardTitle>
                <CardDescription className="text-center">Tạo tài khoản mới để tham gia cộng đồng</CardDescription>
            </CardHeader>
            <CardContent>
                <div className="mb-6 p-4 bg-muted/50 rounded-lg border">
                    <h3 className="text-sm font-medium mb-3 text-center">Tài khoản test</h3>
                    <Button
                        type="button"
                        variant="outline"
                        size="sm"
                        className="w-full justify-start bg-transparent"
                        onClick={fillTestCredentials}
                    >
                        <User className="h-4 w-4 mr-2"/>
                        Người dùng: testuser@test.com / 12345678
                    </Button>
                </div>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div className="flex flex-col items-center space-y-2">
                        <Avatar className="w-20 h-20">
                            <AvatarImage src={avatarPreview || undefined}/>
                            <AvatarFallback>
                                <User className="w-8 h-8"/>
                            </AvatarFallback>
                        </Avatar>
                        <Label htmlFor="avatar" className="cursor-pointer">
                            <div className="flex items-center space-x-2 text-sm text-primary hover:text-primary/80">
                                <Upload className="w-4 h-4"/>
                                <span>Tải ảnh đại diện</span>
                            </div>
                            <Input id="avatar" type="file" accept="image/*" className="hidden"
                                   onChange={handleAvatarChange}/>
                        </Label>
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="fullName">Họ và tên</Label>
                        <div className="relative">
                            <User className="absolute left-3 top-3 h-4 w-4 text-muted-foreground"/>
                            <Input
                                id="fullName"
                                type="text"
                                placeholder="Nguyễn Văn A"
                                className="pl-10"
                                value={formData.fullName}
                                onChange={(e) => setFormData({...formData, fullName: e.target.value})}
                                required
                            />
                        </div>
                    </div>

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
                                onChange={(e) => setFormData({...formData, email: e.target.value})}
                                required
                            />
                        </div>
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="dateOfBirth">Ngày sinh</Label>
                        <div className="relative">
                            <Calendar className="absolute left-3 top-3 h-4 w-4 text-muted-foreground"/>
                            <Input
                                id="dateOfBirth"
                                type="date"
                                className="pl-10"
                                value={formData.dateOfBirth}
                                onChange={(e) => setFormData({...formData, dateOfBirth: e.target.value})}
                                required
                            />
                        </div>
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="gender">Giới tính</Label>
                        <Select
                            value={formData.gender}
                            onValueChange={(value) => setFormData({...formData, gender: value})}
                            required
                        >
                            <SelectTrigger id="gender">
                                <SelectValue placeholder="Chọn giới tính"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="male">Nam</SelectItem>
                                <SelectItem value="female">Nữ</SelectItem>
                                <SelectItem value="other">Khác</SelectItem>
                            </SelectContent>
                        </Select>
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
                                onChange={(e) => setFormData({...formData, password: e.target.value})}
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

                    <div className="space-y-2">
                        <Label htmlFor="confirmPassword">Xác nhận mật khẩu</Label>
                        <div className="relative">
                            <Lock className="absolute left-3 top-3 h-4 w-4 text-muted-foreground"/>
                            <Input
                                id="confirmPassword"
                                type={showConfirmPassword ? "text" : "password"}
                                placeholder="••••••••"
                                className="pl-10 pr-10"
                                value={formData.confirmPassword}
                                onChange={(e) => setFormData({...formData, confirmPassword: e.target.value})}
                                required
                            />
                            <Button
                                type="button"
                                variant="ghost"
                                size="sm"
                                className="absolute right-0 top-0 h-full px-3 py-2 hover:bg-transparent"
                                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                            >
                                {showConfirmPassword ? (
                                    <EyeOff className="h-4 w-4 text-muted-foreground"/>
                                ) : (
                                    <Eye className="h-4 w-4 text-muted-foreground"/>
                                )}
                            </Button>
                        </div>
                    </div>

                    <Button type="submit" className="w-full" disabled={isLoading}>
                        {isLoading ? "Đang đăng ký..." : "Đăng ký"}
                    </Button>
                </form>
            </CardContent>
        </Card>
    )
}
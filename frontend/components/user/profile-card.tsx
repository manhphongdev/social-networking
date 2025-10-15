"use client"

import { useEffect, useState } from "react"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Calendar, Camera, Edit, Heart, MapPin, MessageCircle, Users } from "lucide-react"
import Link from "next/link"
import dayjs from "dayjs"

type UserType = {
    name: string
    avatar: string
    coverImage?: string
    bio: string
    location: string
    joinDate: string
    followers: number
    following: number
    posts: number
    isOwnProfile: boolean
}

export function ProfileCard() {
    const [user, setUser] = useState<UserType | null>(null)
    const [isFollowing, setIsFollowing] = useState(false)

    const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL

    useEffect(() => {
        const token = localStorage.getItem("accessToken")
        if (!token) return

        async function fetchProfile() {
            const res = await fetch(API_BASE_URL + `/users/profile/me`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            })
            const json = await res.json()
            const raw = json.data

            setUser({
                name: raw.name,
                avatar: raw.avatarUrl,
                coverImage: raw.coverImageUrl,
                bio: raw.bio,
                location: raw.location || "",
                joinDate: raw.createdAt,
                followers: raw.totalFollowers || 0,
                following: raw.totalFollowing || 0,
                posts: raw.totalPosts || 0,
                isOwnProfile: true, // luôn true vì lấy theo token
            })
        }

        fetchProfile().catch(console.error)
    }, [])

    if (!user) return <p>Loading...</p>

    return (
        <Card className="w-full">
            {/* Cover Image */}
            <div className="relative h-48 bg-gradient-to-r from-primary/20 to-primary/10 rounded-t-lg overflow-hidden">
                {user.coverImage ? (
                    <img src={user.coverImage} alt="Cover" className="w-full h-full object-cover" />
                ) : (
                    <div className="w-full h-full bg-gradient-to-r from-primary/20 to-primary/10" />
                )}
                {user.isOwnProfile && (
                    <Button variant="secondary" size="sm" className="absolute top-4 right-4">
                        <Camera className="h-4 w-4 mr-2" />
                        Đổi ảnh bìa
                    </Button>
                )}
            </div>

            <CardHeader className="relative pb-2">
                {/* Avatar */}
                <div className="absolute -top-16 left-6">
                    <div className="relative">
                        <Avatar className="w-32 h-32 border-4 border-background">
                            <AvatarImage src={user.avatar} />
                            <AvatarFallback className="text-2xl">{user.name[0]}</AvatarFallback>
                        </Avatar>
                        {user.isOwnProfile && (
                            <Button
                                variant="secondary"
                                size="sm"
                                className="absolute bottom-2 right-2 w-8 h-8 rounded-full p-0"
                            >
                                <Camera className="h-4 w-4" />
                            </Button>
                        )}
                    </div>
                </div>

                {/* Action Buttons */}
                <div className="flex justify-end pt-4">
                    {user.isOwnProfile ? (
                        <Button variant="outline" asChild>
                            <Link href="/profile/edit">
                                <Edit className="h-4 w-4 mr-2" />
                                Chỉnh sửa hồ sơ
                            </Link>
                        </Button>
                    ) : (
                        <div className="flex space-x-2">
                            <Button
                                variant={isFollowing ? "secondary" : "default"}
                                onClick={() => setIsFollowing(!isFollowing)}
                            >
                                <Users className="h-4 w-4 mr-2" />
                                {isFollowing ? "Đang theo dõi" : "Theo dõi"}
                            </Button>
                            <Button variant="outline">
                                <MessageCircle className="h-4 w-4 mr-2" />
                                Nhắn tin
                            </Button>
                        </div>
                    )}
                </div>
            </CardHeader>

            <CardContent className="pt-8">
                <div className="space-y-4">
                    <p className="text-muted-foreground">{user.name}</p>

                    {user.bio && <p className="text-foreground">{user.bio}</p>}

                    <div className="flex flex-wrap gap-4 text-sm text-muted-foreground">
                        {user.location && (
                            <div className="flex items-center">
                                <MapPin className="h-4 w-4 mr-1" />
                                {user.location}
                            </div>
                        )}
                        {user.joinDate && (
                            <div className="flex items-center">
                                <Calendar className="h-4 w-4 mr-1" />
                                Tham gia {dayjs(user.joinDate).locale("vi").format("DD-MM-YYYY")}
                            </div>
                        )}
                    </div>

                    <div className="flex space-x-6">
                        <div className="text-center">
                            <p className="font-bold text-lg">{user.posts}</p>
                            <p className="text-sm text-muted-foreground">Bài viết</p>
                        </div>
                        <div className="text-center">
                            <p className="font-bold text-lg">{user.followers}</p>
                            <p className="text-sm text-muted-foreground">Người theo dõi</p>
                        </div>
                        <div className="text-center">
                            <p className="font-bold text-lg">{user.following}</p>
                            <p className="text-sm text-muted-foreground">Đang theo dõi</p>
                        </div>
                    </div>

                    <div className="flex flex-wrap gap-2">
                        <Badge variant="secondary">
                            <Heart className="h-3 w-3 mr-1" />
                            Thành viên tích cực
                        </Badge>
                        <Badge variant="outline">Người sáng tạo nội dung</Badge>
                    </div>
                </div>
            </CardContent>
        </Card>
    )
}

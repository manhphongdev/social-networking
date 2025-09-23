"use client"

import { useState } from "react"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { MapPin, Calendar, Users, Heart, MessageCircle, Edit, Camera } from "lucide-react"

interface ProfileCardProps {
  user: {
    id: string
    name: string
    username: string
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
}

export function ProfileCard({ user }: ProfileCardProps) {
  const [isFollowing, setIsFollowing] = useState(false)

  return (
    <Card className="w-full">
      {/* Cover Image */}
      <div className="relative h-48 bg-gradient-to-r from-primary/20 to-primary/10 rounded-t-lg overflow-hidden">
        {user.coverImage ? (
          <img src={user.coverImage || "/placeholder.svg"} alt="Cover" className="w-full h-full object-cover" />
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
              <AvatarImage src={user.avatar || "/placeholder.svg"} />
              <AvatarFallback className="text-2xl">{user.name[0]}</AvatarFallback>
            </Avatar>
            {user.isOwnProfile && (
              <Button variant="secondary" size="sm" className="absolute bottom-2 right-2 w-8 h-8 rounded-full p-0">
                <Camera className="h-4 w-4" />
              </Button>
            )}
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex justify-end pt-4">
          {user.isOwnProfile ? (
            <Button variant="outline">
              <Edit className="h-4 w-4 mr-2" />
              Chỉnh sửa hồ sơ
            </Button>
          ) : (
            <div className="flex space-x-2">
              <Button variant={isFollowing ? "secondary" : "default"} onClick={() => setIsFollowing(!isFollowing)}>
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
        {/* User Info */}
        <div className="space-y-4">
          <div>
            <h1 className="text-2xl font-bold">{user.name}</h1>
            <p className="text-muted-foreground">@{user.username}</p>
          </div>

          <p className="text-foreground">{user.bio}</p>

          {/* User Details */}
          <div className="flex flex-wrap gap-4 text-sm text-muted-foreground">
            <div className="flex items-center">
              <MapPin className="h-4 w-4 mr-1" />
              {user.location}
            </div>
            <div className="flex items-center">
              <Calendar className="h-4 w-4 mr-1" />
              Tham gia {user.joinDate}
            </div>
          </div>

          {/* Stats */}
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

          {/* Badges */}
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

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Heart, MessageCircle, UserPlus, Share, MoreHorizontal } from "lucide-react"

interface NotificationItemProps {
  notification: {
    id: string
    type: "like" | "comment" | "follow" | "share"
    user: {
      name: string
      avatar: string
      username: string
    }
    content?: string
    post?: {
      id: string
      content: string
      image?: string
    }
    timestamp: string
    isRead: boolean
  }
}

export function NotificationItem({ notification }: NotificationItemProps) {
  const getIcon = () => {
    switch (notification.type) {
      case "like":
        return <Heart className="h-4 w-4 text-red-500" />
      case "comment":
        return <MessageCircle className="h-4 w-4 text-blue-500" />
      case "follow":
        return <UserPlus className="h-4 w-4 text-green-500" />
      case "share":
        return <Share className="h-4 w-4 text-purple-500" />
      default:
        return null
    }
  }

  const getActionText = () => {
    switch (notification.type) {
      case "like":
        return "đã thích bài viết của bạn"
      case "comment":
        return "đã bình luận về bài viết của bạn"
      case "follow":
        return "đã bắt đầu theo dõi bạn"
      case "share":
        return "đã chia sẻ bài viết của bạn"
      default:
        return ""
    }
  }

  return (
    <Card className={`w-full ${!notification.isRead ? "bg-primary/5 border-primary/20" : ""}`}>
      <CardContent className="p-4">
        <div className="flex items-start space-x-3">
          <div className="relative">
            <Avatar className="w-10 h-10">
              <AvatarImage src={notification.user.avatar || "/placeholder.svg"} />
              <AvatarFallback>{notification.user.name[0]}</AvatarFallback>
            </Avatar>
            <div className="absolute -bottom-1 -right-1 bg-background rounded-full p-1">{getIcon()}</div>
          </div>

          <div className="flex-1 min-w-0">
            <div className="flex items-start justify-between">
              <div className="flex-1">
                <p className="text-sm">
                  <span className="font-semibold">{notification.user.name}</span>{" "}
                  <span className="text-muted-foreground">{getActionText()}</span>
                </p>

                {notification.content && <p className="text-sm text-muted-foreground mt-1">"{notification.content}"</p>}

                {notification.post && (
                  <div className="mt-2 p-2 bg-muted rounded-lg">
                    <p className="text-xs text-muted-foreground line-clamp-2">{notification.post.content}</p>
                    {notification.post.image && (
                      <div className="mt-2">
                        <img
                          src={notification.post.image || "/placeholder.svg"}
                          alt="Post"
                          className="w-16 h-16 object-cover rounded"
                        />
                      </div>
                    )}
                  </div>
                )}

                <p className="text-xs text-muted-foreground mt-2">{notification.timestamp}</p>
              </div>

              <div className="flex items-center space-x-2">
                {!notification.isRead && <Badge variant="default" className="h-2 w-2 p-0 rounded-full" />}
                <Button variant="ghost" size="sm">
                  <MoreHorizontal className="h-4 w-4" />
                </Button>
              </div>
            </div>

            {notification.type === "follow" && (
              <div className="mt-3">
                <Button size="sm" variant="outline">
                  Theo dõi lại
                </Button>
              </div>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

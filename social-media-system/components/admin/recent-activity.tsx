import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"

const recentActivities = [
  {
    id: "1",
    user: {
      name: "Nguyễn Văn A",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    action: "đã đăng ký tài khoản mới",
    timestamp: "5 phút trước",
    type: "user_register",
  },
  {
    id: "2",
    user: {
      name: "Trần Thị B",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    action: "đã báo cáo một bài viết",
    timestamp: "15 phút trước",
    type: "report",
  },
  {
    id: "3",
    user: {
      name: "Lê Văn C",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    action: "đã đăng bài viết mới",
    timestamp: "30 phút trước",
    type: "post",
  },
  {
    id: "4",
    user: {
      name: "Phạm Thị D",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    action: "đã cập nhật hồ sơ",
    timestamp: "1 giờ trước",
    type: "profile_update",
  },
]

export function RecentActivity() {
  const getActivityBadge = (type: string) => {
    switch (type) {
      case "user_register":
        return <Badge variant="default">Đăng ký</Badge>
      case "report":
        return <Badge variant="destructive">Báo cáo</Badge>
      case "post":
        return <Badge variant="secondary">Bài viết</Badge>
      case "profile_update":
        return <Badge variant="outline">Cập nhật</Badge>
      default:
        return <Badge variant="outline">Khác</Badge>
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Hoạt động gần đây</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {recentActivities.map((activity) => (
            <div key={activity.id} className="flex items-center space-x-3">
              <Avatar className="w-8 h-8">
                <AvatarImage src={activity.user.avatar || "/placeholder.svg"} />
                <AvatarFallback>{activity.user.name[0]}</AvatarFallback>
              </Avatar>
              <div className="flex-1 min-w-0">
                <p className="text-sm">
                  <span className="font-medium">{activity.user.name}</span>{" "}
                  <span className="text-muted-foreground">{activity.action}</span>
                </p>
                <p className="text-xs text-muted-foreground">{activity.timestamp}</p>
              </div>
              {getActivityBadge(activity.type)}
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  )
}

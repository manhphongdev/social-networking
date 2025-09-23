import { UserNavbar } from "@/components/user/user-navbar"
import { NotificationItem } from "@/components/user/notification-item"
import { Button } from "@/components/ui/button"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"

const sampleNotifications = [
  {
    id: "1",
    type: "like" as const,
    user: {
      name: "Nguyễn Văn A",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "nguyenvana",
    },
    post: {
      id: "1",
      content: "Hôm nay thật là một ngày tuyệt vời! Vừa hoàn thành dự án mới...",
      image: "/placeholder.svg?height=100&width=100",
    },
    timestamp: "5 phút trước",
    isRead: false,
  },
  {
    id: "2",
    type: "comment" as const,
    user: {
      name: "Trần Thị B",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "tranthib",
    },
    content: "Bài viết rất hay! Cảm ơn bạn đã chia sẻ.",
    post: {
      id: "2",
      content: "Chia sẻ một số tips học lập trình hiệu quả...",
    },
    timestamp: "15 phút trước",
    isRead: false,
  },
  {
    id: "3",
    type: "follow" as const,
    user: {
      name: "Lê Văn C",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "levanc",
    },
    timestamp: "1 giờ trước",
    isRead: true,
  },
  {
    id: "4",
    type: "share" as const,
    user: {
      name: "Phạm Thị D",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "phamthid",
    },
    post: {
      id: "3",
      content: "Sunset tại Đà Lạt hôm nay thật đẹp!",
      image: "/placeholder.svg?height=100&width=100",
    },
    timestamp: "2 giờ trước",
    isRead: true,
  },
]

export default function NotificationsPage() {
  const unreadCount = sampleNotifications.filter((n) => !n.isRead).length

  return (
    <div className="min-h-screen bg-background">
      <UserNavbar />
      <div className="container mx-auto px-4 py-6">
        <div className="max-w-2xl mx-auto">
          <div className="flex items-center justify-between mb-6">
            <h1 className="text-2xl font-bold">
              Thông báo
              {unreadCount > 0 && (
                <span className="ml-2 bg-primary text-primary-foreground text-sm px-2 py-1 rounded-full">
                  {unreadCount}
                </span>
              )}
            </h1>
            <Button variant="outline" size="sm">
              Đánh dấu tất cả đã đọc
            </Button>
          </div>

          <Tabs defaultValue="all" className="w-full">
            <TabsList className="grid w-full grid-cols-4">
              <TabsTrigger value="all">Tất cả</TabsTrigger>
              <TabsTrigger value="likes">Thích</TabsTrigger>
              <TabsTrigger value="comments">Bình luận</TabsTrigger>
              <TabsTrigger value="follows">Theo dõi</TabsTrigger>
            </TabsList>

            <TabsContent value="all" className="space-y-4 mt-6">
              {sampleNotifications.map((notification) => (
                <NotificationItem key={notification.id} notification={notification} />
              ))}
            </TabsContent>

            <TabsContent value="likes" className="space-y-4 mt-6">
              {sampleNotifications
                .filter((n) => n.type === "like")
                .map((notification) => (
                  <NotificationItem key={notification.id} notification={notification} />
                ))}
            </TabsContent>

            <TabsContent value="comments" className="space-y-4 mt-6">
              {sampleNotifications
                .filter((n) => n.type === "comment")
                .map((notification) => (
                  <NotificationItem key={notification.id} notification={notification} />
                ))}
            </TabsContent>

            <TabsContent value="follows" className="space-y-4 mt-6">
              {sampleNotifications
                .filter((n) => n.type === "follow")
                .map((notification) => (
                  <NotificationItem key={notification.id} notification={notification} />
                ))}
            </TabsContent>
          </Tabs>
        </div>
      </div>
    </div>
  )
}

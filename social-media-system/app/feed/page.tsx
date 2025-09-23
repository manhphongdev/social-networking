import { UserNavbar } from "@/components/user/user-navbar"
import { CreatePost } from "@/components/user/create-post"
import { PostCard } from "@/components/user/post-card"

const samplePosts = [
  {
    id: "1",
    author: {
      name: "Nguyễn Văn A",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "nguyenvana",
    },
    content:
      "Hôm nay thật là một ngày tuyệt vời! Vừa hoàn thành dự án mới và cảm thấy rất hài lòng với kết quả. Cảm ơn team đã hỗ trợ nhiệt tình! 🎉",
    image: "/celebration-team-work.jpg",
    timestamp: "2 giờ trước",
    likes: 24,
    comments: 8,
    shares: 3,
    isLiked: false,
    isFollowing: false,
  },
  {
    id: "2",
    author: {
      name: "Trần Thị B",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "tranthib",
    },
    content:
      "Chia sẻ một số tips học lập trình hiệu quả mà mình đã áp dụng:\n\n1. Thực hành mỗi ngày\n2. Đọc code của người khác\n3. Tham gia cộng đồng\n4. Xây dựng dự án thực tế\n\nCác bạn có tips nào khác không?",
    timestamp: "4 giờ trước",
    likes: 156,
    comments: 23,
    shares: 12,
    isLiked: true,
    isFollowing: true,
  },
  {
    id: "3",
    author: {
      name: "Lê Văn C",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "levanc",
    },
    content: "Sunset tại Đà Lạt hôm nay thật đẹp! Không thể tin được mình đã bỏ lỡ cảnh này bao lâu rồi.",
    image: "/beautiful-sunset-dalat-vietnam.jpg",
    timestamp: "6 giờ trước",
    likes: 89,
    comments: 15,
    shares: 7,
    isLiked: false,
    isFollowing: false,
  },
]

export default function FeedPage() {
  return (
    <div className="min-h-screen bg-background">
      <UserNavbar />
      <div className="container mx-auto px-4 py-6">
        <div className="max-w-2xl mx-auto space-y-6">
          <CreatePost />
          {samplePosts.map((post) => (
            <PostCard key={post.id} post={post} />
          ))}
        </div>
      </div>
    </div>
  )
}

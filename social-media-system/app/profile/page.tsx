import { UserNavbar } from "@/components/user/user-navbar"
import { ProfileCard } from "@/components/user/profile-card"
import { PostCard } from "@/components/user/post-card"

const sampleUser = {
  id: "1",
  name: "Nguyễn Văn User",
  username: "nguyenvanuser",
  avatar: "/placeholder.svg?height=128&width=128",
  coverImage: "/beautiful-landscape-cover.jpg",
  bio: "Lập trình viên Full-stack | Yêu thích công nghệ và du lịch | Luôn học hỏi những điều mới mỗi ngày 🚀",
  location: "Hồ Chí Minh, Việt Nam",
  joinDate: "tháng 3 năm 2022",
  followers: 1234,
  following: 567,
  posts: 89,
  isOwnProfile: true,
}

const userPosts = [
  {
    id: "1",
    author: {
      name: "Nguyễn Văn User",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "nguyenvanuser",
    },
    content:
      "Vừa hoàn thành khóa học React Advanced! Cảm thấy tự tin hơn nhiều với các concept như Context, Hooks và Performance Optimization.",
    timestamp: "1 ngày trước",
    likes: 45,
    comments: 12,
    shares: 5,
    isLiked: false,
    isFollowing: false,
  },
  {
    id: "2",
    author: {
      name: "Nguyễn Văn User",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "nguyenvanuser",
    },
    content:
      "Chia sẻ project mới: Social Media Dashboard built with Next.js và TailwindCSS. Feedback từ mọi người sẽ rất có ích!",
    image: "/modern-dashboard.png",
    timestamp: "3 ngày trước",
    likes: 78,
    comments: 18,
    shares: 9,
    isLiked: false,
    isFollowing: false,
  },
]

export default function ProfilePage() {
  return (
    <div className="min-h-screen bg-background">
      <UserNavbar />
      <div className="container mx-auto px-4 py-6">
        <div className="max-w-4xl mx-auto space-y-6">
          <ProfileCard user={sampleUser} />

          <div className="max-w-2xl mx-auto space-y-6">
            <h2 className="text-xl font-semibold">Bài viết của bạn</h2>
            {userPosts.map((post) => (
              <PostCard key={post.id} post={post} />
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

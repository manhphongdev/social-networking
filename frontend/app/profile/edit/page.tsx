"use client"
import { useRouter } from "next/navigation"
import { UserNavbar } from "@/components/user/user-navbar"
import { EditProfileForm } from "@/components/user/edit-profile-form"

const currentUser = {
  name: "Nguyễn Văn User",
  username: "nguyenvanuser",
  bio: "Lập trình viên Full-stack | Yêu thích công nghệ và du lịch | Luôn học hỏi những điều mới mỗi ngày 🚀",
  location: "Hồ Chí Minh, Việt Nam",
  birthDate: "1995-06-15",
  gender: "male",
  avatar: "/placeholder.svg?height=128&width=128",
}

export default function EditProfilePage() {
  const router = useRouter()

  const handleSave = (data: any) => {
    // Handle save logic here
    console.log("Saving profile:", data)
    router.push("/profile")
  }

  const handleCancel = () => {
    router.push("/profile")
  }

  return (
    <div className="min-h-screen bg-background">
      <UserNavbar />
      <div className="container mx-auto px-4 py-6">
        <EditProfileForm user={currentUser} onSave={handleSave} onCancel={handleCancel} />
      </div>
    </div>
  )
}

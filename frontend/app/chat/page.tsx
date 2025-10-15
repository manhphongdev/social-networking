"use client"

import { useState } from "react"
import { UserNavbar } from "@/components/user/user-navbar"
import { ChatSidebar } from "@/components/user/chat-sidebar"
import { ChatWindow } from "@/components/user/chat-window"

const sampleConversations = [
  {
    id: "1",
    user: {
      name: "Nguyễn Văn A",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "nguyenvana",
    },
    lastMessage: "Cảm ơn bạn đã chia sẻ!",
    timestamp: "5 phút",
    unreadCount: 2,
    isOnline: true,
  },
  {
    id: "2",
    user: {
      name: "Trần Thị B",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "tranthib",
    },
    lastMessage: "Hẹn gặp lại bạn sau nhé",
    timestamp: "1 giờ",
    unreadCount: 0,
    isOnline: false,
  },
  {
    id: "3",
    user: {
      name: "Lê Văn C",
      avatar: "/placeholder.svg?height=40&width=40",
      username: "levanc",
    },
    lastMessage: "Bạn có rảnh không?",
    timestamp: "2 giờ",
    unreadCount: 1,
    isOnline: true,
  },
]

const sampleMessages = [
  {
    id: "1",
    content: "Chào bạn! Bạn có khỏe không?",
    timestamp: "10:30",
    isOwn: false,
    type: "text" as const,
  },
  {
    id: "2",
    content: "Chào! Mình khỏe, cảm ơn bạn. Bạn thì sao?",
    timestamp: "10:32",
    isOwn: true,
    type: "text" as const,
  },
  {
    id: "3",
    content: "Mình cũng ổn. Hôm nay có dự án mới rất thú vị!",
    timestamp: "10:35",
    isOwn: false,
    type: "text" as const,
  },
  {
    id: "4",
    content: "/exciting-new-project.jpg",
    timestamp: "10:36",
    isOwn: false,
    type: "image" as const,
    imageUrl: "/exciting-new-project.jpg",
  },
  {
    id: "5",
    content: "Wow! Trông có vẻ rất thú vị đấy!",
    timestamp: "10:38",
    isOwn: true,
    type: "text" as const,
  },
]

export default function ChatPage() {
  const [activeConversationId, setActiveConversationId] = useState("1")

  const activeConversation = sampleConversations.find((conv) => conv.id === activeConversationId)

  return (
    <div className="min-h-screen bg-background">
      <UserNavbar />
      <div className="flex h-[calc(100vh-4rem)]">
        <ChatSidebar
          conversations={sampleConversations}
          activeConversationId={activeConversationId}
          onConversationSelect={setActiveConversationId}
        />

        {activeConversation ? (
          <ChatWindow conversation={activeConversation} messages={sampleMessages} />
        ) : (
          <div className="flex-1 flex items-center justify-center">
            <p className="text-muted-foreground">Chọn một cuộc trò chuyện để bắt đầu</p>
          </div>
        )}
      </div>
    </div>
  )
}

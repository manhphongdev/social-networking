"use client"

import {useState} from "react"
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar"
import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {Badge} from "@/components/ui/badge"
import {Plus, Search} from "lucide-react"

interface ChatSidebarProps {
    conversations: Array<{
        id: string
        user: {
            name: string
            avatar: string
            username: string
        }
        lastMessage: string
        timestamp: string
        unreadCount: number
        isOnline: boolean
    }>
    activeConversationId?: string
    onConversationSelect: (id: string) => void
}

export function ChatSidebar({conversations, activeConversationId, onConversationSelect}: ChatSidebarProps) {

    const [searchQuery, setSearchQuery] = useState("")

    const filteredConversations = conversations.filter(
        (conv) =>
            conv.user.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            conv.user.username.toLowerCase().includes(searchQuery.toLowerCase()),
    )

    return (
        <div className="w-80 border-r bg-card h-full flex flex-col">
            {/* Header */}
            <div className="p-4 border-b">
                <div className="flex items-center justify-between mb-4">
                    <h2 className="text-lg font-semibold">Tin nhắn</h2>
                    <Button variant="ghost" size="sm">
                        <Plus className="h-4 w-4"/>
                    </Button>
                </div>

                {/* Search */}
                <div className="relative">
                    <Search
                        className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground"/>
                    <Input
                        placeholder="Tìm kiếm cuộc trò chuyện..."
                        className="pl-10"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                </div>
            </div>

            {/* Conversations List */}
            <div className="flex-1 overflow-y-auto">
                {filteredConversations.map((conversation) => (
                    <div
                        key={conversation.id}
                        className={`p-4 border-b cursor-pointer hover:bg-accent transition-colors ${
                            activeConversationId === conversation.id ? "bg-accent" : ""
                        }`}
                        onClick={() => onConversationSelect(conversation.id)}
                    >
                        <div className="flex items-center space-x-3">
                            <div className="relative">
                                <Avatar>
                                    <AvatarImage src={conversation.user.avatar || "/placeholder.svg"}/>
                                    <AvatarFallback>{conversation.user.name[0]}</AvatarFallback>
                                </Avatar>
                                {conversation.isOnline && (
                                    <div
                                        className="absolute bottom-0 right-0 w-3 h-3 bg-green-500 rounded-full border-2 border-background"/>
                                )}
                            </div>

                            <div className="flex-1 min-w-0">
                                <div className="flex items-center justify-between">
                                    <p className="font-medium truncate">{conversation.user.name}</p>
                                    <div className="flex items-center space-x-2">
                                        <span className="text-xs text-muted-foreground">{conversation.timestamp}</span>
                                        {conversation.unreadCount > 0 && (
                                            <Badge variant="default"
                                                   className="h-5 w-5 p-0 text-xs flex items-center justify-center">
                                                {conversation.unreadCount}
                                            </Badge>
                                        )}
                                    </div>
                                </div>
                                <p className="text-sm text-muted-foreground truncate">{conversation.lastMessage}</p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

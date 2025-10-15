"use client"

import { useState } from "react"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Heart, MessageCircle, Share, MoreHorizontal, ThumbsUp, Laugh, Angry, Salad as Sad } from "lucide-react"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"

interface Comment {
  id: string
  author: {
    name: string
    avatar: string
  }
  content: string
  timestamp: string
  likes: number
  isLiked: boolean
  replies: Comment[]
}

interface PostCardProps {
  post: {
    id: string
    author: {
      name: string
      avatar: string
      username: string
    }
    content: string
    image?: string
    timestamp: string
    likes: number
    comments: number
    shares: number
    isLiked: boolean
    isFollowing: boolean
  }
}

export function PostCard({ post }: PostCardProps) {
  const [isLiked, setIsLiked] = useState(post.isLiked)
  const [isFollowing, setIsFollowing] = useState(post.isFollowing)
  const [showComments, setShowComments] = useState(false)
  const [newComment, setNewComment] = useState("")
  const [likesCount, setLikesCount] = useState(post.likes)
  const [currentReaction, setCurrentReaction] = useState<string | null>(post.isLiked ? "like" : null)

  const [comments, setComments] = useState<Comment[]>([
    {
      id: "1",
      author: { name: "Nguyễn Văn A", avatar: "/placeholder.svg?height=32&width=32" },
      content: "Bài viết rất hay! Cảm ơn bạn đã chia sẻ.",
      timestamp: "2 giờ trước",
      likes: 3,
      isLiked: false,
      replies: [],
    },
    {
      id: "2",
      author: { name: "Trần Thị B", avatar: "/placeholder.svg?height=32&width=32" },
      content: "Mình cũng nghĩ vậy, rất bổ ích!",
      timestamp: "1 giờ trước",
      likes: 1,
      isLiked: true,
      replies: [],
    },
  ])
  const [replyingTo, setReplyingTo] = useState<string | null>(null)
  const [replyText, setReplyText] = useState("")

  const reactions = [
    { icon: ThumbsUp, label: "Like", color: "text-blue-500", key: "like" },
    { icon: Heart, label: "Love", color: "text-red-500", key: "love" },
    { icon: Laugh, label: "Haha", color: "text-yellow-500", key: "haha" },
    { icon: Angry, label: "Angry", color: "text-red-600", key: "angry" },
    { icon: Sad, label: "Sad", color: "text-blue-600", key: "sad" },
  ]

  const handleReaction = (reactionKey: string) => {
    if (currentReaction === reactionKey) {
      setCurrentReaction(null)
      setIsLiked(false)
      setLikesCount((prev) => prev - 1)
    } else {
      const wasLiked = currentReaction !== null
      setCurrentReaction(reactionKey)
      setIsLiked(true)
      setLikesCount((prev) => (wasLiked ? prev : prev + 1))
    }
  }

  const handleAddComment = () => {
    if (newComment.trim()) {
      const comment: Comment = {
        id: Date.now().toString(),
        author: { name: "Bạn", avatar: "/placeholder.svg?height=32&width=32" },
        content: newComment,
        timestamp: "Vừa xong",
        likes: 0,
        isLiked: false,
        replies: [],
      }
      setComments((prev) => [...prev, comment])
      setNewComment("")
    }
  }

  const handleReply = (commentId: string) => {
    if (replyText.trim()) {
      const reply: Comment = {
        id: Date.now().toString(),
        author: { name: "Bạn", avatar: "/placeholder.svg?height=32&width=32" },
        content: replyText,
        timestamp: "Vừa xong",
        likes: 0,
        isLiked: false,
        replies: [],
      }

      setComments((prev) =>
        prev.map((comment) =>
          comment.id === commentId ? { ...comment, replies: [...comment.replies, reply] } : comment,
        ),
      )
      setReplyText("")
      setReplyingTo(null)
    }
  }

  const handleCommentLike = (commentId: string) => {
    setComments((prev) =>
      prev.map((comment) =>
        comment.id === commentId
          ? {
              ...comment,
              isLiked: !comment.isLiked,
              likes: comment.isLiked ? comment.likes - 1 : comment.likes + 1,
            }
          : comment,
      ),
    )
  }

  return (
    <Card className="w-full">
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <Avatar>
              <AvatarImage src={post.author.avatar || "/placeholder.svg"} />
              <AvatarFallback>{post.author.name[0]}</AvatarFallback>
            </Avatar>
            <div>
              <p className="font-semibold">{post.author.name}</p>
              <p className="text-sm text-muted-foreground">
                @{post.author.username} • {post.timestamp}
              </p>
            </div>
          </div>
          <div className="flex items-center space-x-2">
            <Button
              variant={isFollowing ? "secondary" : "default"}
              size="sm"
              onClick={() => setIsFollowing(!isFollowing)}
            >
              {isFollowing ? "Đang theo dõi" : "Theo dõi"}
            </Button>
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="sm">
                  <MoreHorizontal className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent>
                <DropdownMenuItem>Báo cáo bài viết</DropdownMenuItem>
                <DropdownMenuItem>Ẩn bài viết</DropdownMenuItem>
                <DropdownMenuItem>Sao chép liên kết</DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        <p className="text-foreground">{post.content}</p>

        {post.image && (
          <div className="rounded-lg overflow-hidden">
            <img src={post.image || "/placeholder.svg"} alt="Post content" className="w-full h-auto object-cover" />
          </div>
        )}

        {/* Reaction Stats */}
        <div className="flex items-center justify-between text-sm text-muted-foreground">
          <div className="flex items-center space-x-4">
            <span>{likesCount} lượt thích</span>
            <span>{comments.length} bình luận</span>
          </div>
          <span>{post.shares} lượt chia sẻ</span>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center justify-between pt-2 border-t">
          <div className="flex items-center space-x-1">
            <Button
              variant="ghost"
              size="sm"
              className={`flex items-center space-x-2 ${isLiked ? "text-red-500" : ""}`}
              onClick={() => handleReaction("like")}
            >
              <Heart className={`h-4 w-4 ${isLiked ? "fill-current" : ""}`} />
              <span>Thích</span>
            </Button>

            {/* Reaction Dropdown */}
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="sm" className="px-2">
                  <span className="text-xs">▼</span>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent className="flex p-2 space-x-1">
                {reactions.map((reaction) => (
                  <Button
                    key={reaction.key}
                    variant="ghost"
                    size="sm"
                    className={`p-2 ${reaction.color} ${currentReaction === reaction.key ? "bg-muted" : ""}`}
                    onClick={() => handleReaction(reaction.key)}
                  >
                    <reaction.icon className="h-5 w-5" />
                  </Button>
                ))}
              </DropdownMenuContent>
            </DropdownMenu>
          </div>

          <Button
            variant="ghost"
            size="sm"
            className="flex items-center space-x-2"
            onClick={() => setShowComments(!showComments)}
          >
            <MessageCircle className="h-4 w-4" />
            <span>Bình luận</span>
          </Button>

          <Button variant="ghost" size="sm" className="flex items-center space-x-2">
            <Share className="h-4 w-4" />
            <span>Chia sẻ</span>
          </Button>
        </div>

        {/* Comments Section */}
        {showComments && (
          <div className="space-y-3 pt-3 border-t">
            <div className="flex space-x-3">
              <Avatar className="w-8 h-8">
                <AvatarImage src="/placeholder.svg?height=32&width=32" />
                <AvatarFallback>B</AvatarFallback>
              </Avatar>
              <div className="flex-1 flex space-x-2">
                <Input
                  placeholder="Viết bình luận..."
                  value={newComment}
                  onChange={(e) => setNewComment(e.target.value)}
                  className="flex-1"
                  onKeyPress={(e) => e.key === "Enter" && handleAddComment()}
                />
                <Button size="sm" onClick={handleAddComment}>
                  Gửi
                </Button>
              </div>
            </div>

            {/* Comments List */}
            <div className="space-y-3">
              {comments.map((comment) => (
                <div key={comment.id} className="space-y-2">
                  <div className="flex space-x-3">
                    <Avatar className="w-8 h-8">
                      <AvatarImage src={comment.author.avatar || "/placeholder.svg"} />
                      <AvatarFallback>{comment.author.name[0]}</AvatarFallback>
                    </Avatar>
                    <div className="flex-1">
                      <div className="bg-muted rounded-lg p-3">
                        <p className="font-semibold text-sm">{comment.author.name}</p>
                        <p className="text-sm">{comment.content}</p>
                      </div>
                      <div className="flex items-center space-x-4 mt-1 text-xs text-muted-foreground">
                        <span>{comment.timestamp}</span>
                        <Button
                          variant="ghost"
                          size="sm"
                          className={`h-auto p-0 text-xs ${comment.isLiked ? "text-blue-500" : ""}`}
                          onClick={() => handleCommentLike(comment.id)}
                        >
                          Thích ({comment.likes})
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          className="h-auto p-0 text-xs"
                          onClick={() => setReplyingTo(replyingTo === comment.id ? null : comment.id)}
                        >
                          Trả lời
                        </Button>
                      </div>

                      {/* Reply Input */}
                      {replyingTo === comment.id && (
                        <div className="flex space-x-2 mt-2">
                          <Input
                            placeholder="Viết phản hồi..."
                            value={replyText}
                            onChange={(e) => setReplyText(e.target.value)}
                            className="flex-1 text-sm"
                            onKeyPress={(e) => e.key === "Enter" && handleReply(comment.id)}
                          />
                          <Button size="sm" onClick={() => handleReply(comment.id)}>
                            Gửi
                          </Button>
                        </div>
                      )}

                      {/* Replies */}
                      {comment.replies.length > 0 && (
                        <div className="ml-4 mt-2 space-y-2">
                          {comment.replies.map((reply) => (
                            <div key={reply.id} className="flex space-x-2">
                              <Avatar className="w-6 h-6">
                                <AvatarImage src={reply.author.avatar || "/placeholder.svg"} />
                                <AvatarFallback>{reply.author.name[0]}</AvatarFallback>
                              </Avatar>
                              <div className="flex-1">
                                <div className="bg-muted rounded-lg p-2">
                                  <p className="font-semibold text-xs">{reply.author.name}</p>
                                  <p className="text-xs">{reply.content}</p>
                                </div>
                                <div className="flex items-center space-x-2 mt-1 text-xs text-muted-foreground">
                                  <span>{reply.timestamp}</span>
                                  <Button variant="ghost" size="sm" className="h-auto p-0 text-xs">
                                    Thích
                                  </Button>
                                </div>
                              </div>
                            </div>
                          ))}
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  )
}

"use client"

import type React from "react"
import {useState} from "react"
import {Button} from "@/components/ui/button"
import {Textarea} from "@/components/ui/textarea"
import {Card, CardContent, CardHeader} from "@/components/ui/card"
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar"
import {ImageIcon, Smile, X} from "lucide-react"

export function CreatePost() {
    const [content, setContent] = useState("")
    const [selectedImage, setSelectedImage] = useState<string | null>(null)

    const handleImageSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]
        if (file) {
            const reader = new FileReader()
            reader.onload = (e) => {
                setSelectedImage(e.target?.result as string)
            }
            reader.readAsDataURL(file)
        }
    }

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault()
        if (content.trim() || selectedImage) {
            // Handle post creation
            console.log("Creating post:", {content, image: selectedImage})
            setContent("")
            setSelectedImage(null)
        }
    }

    return (
        <Card className="w-full">
            <CardHeader className="pb-3">
                <div className="flex items-center space-x-3">
                    <Avatar>
                        <AvatarImage src="/placeholder.svg?height=40&width=40"/>
                        <AvatarFallback>U</AvatarFallback>
                    </Avatar>
                    <div>
                        <p className="font-semibold">Nguyễn Văn User</p>
                        <p className="text-sm text-muted-foreground">Chia sẻ suy nghĩ của bạn...</p>
                    </div>
                </div>
            </CardHeader>
            <CardContent>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <Textarea
                        placeholder="Bạn đang nghĩ gì?"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        className="min-h-[100px] resize-none border-none p-0 text-lg placeholder:text-muted-foreground focus-visible:ring-0"
                    />

                    {selectedImage && (
                        <div className="relative">
                            <img
                                src={selectedImage || "/placeholder.svg"}
                                alt="Selected"
                                className="w-full max-h-96 object-cover rounded-lg"
                            />
                            <Button
                                type="button"
                                variant="secondary"
                                size="sm"
                                className="absolute top-2 right-2"
                                onClick={() => setSelectedImage(null)}
                            >
                                <X className="h-4 w-4"/>
                            </Button>
                        </div>
                    )}

                    <div className="flex items-center justify-between pt-4 border-t">
                        <div className="flex items-center space-x-2">
                            <label htmlFor="image-upload" className="cursor-pointer">
                                <Button type="button" variant="ghost" size="sm" asChild>
                                    <div>
                                        <ImageIcon className="h-4 w-4 mr-2"/>
                                        Ảnh
                                    </div>
                                </Button>
                                <input id="image-upload" type="file" accept="image/*" className="hidden"
                                       onChange={handleImageSelect}/>
                            </label>
                            <Button type="button" variant="ghost" size="sm">
                                <Smile className="h-4 w-4 mr-2"/>
                                Cảm xúc
                            </Button>

                        </div>

                        <Button type="submit" disabled={!content.trim() && !selectedImage}>
                            Đăng bài
                        </Button>
                    </div>
                </form>
            </CardContent>
        </Card>
    )
}

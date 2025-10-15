"use client"

import {useEffect, useState} from "react"
import {UserNavbar} from "@/components/user/user-navbar"
import {ProfileCard} from "@/components/user/profile-card"
import {PostCard} from "@/components/user/post-card"

type UserType = {
    id: string
    name: string
    username: string
    avatar: string
    coverImage?: string
    bio: string
    location: string
    joinDate: string
    followers: number
    following: number
    posts: number
    isOwnProfile: boolean
}

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL

export default function ProfilePage() {
    const [user, setUser] = useState<UserType | null>(null)
    const [posts, setPosts] = useState<any[]>([])

    useEffect(() => {
        const userId = localStorage.getItem("userId")
        const token = localStorage.getItem("accessToken")
        if (!userId || !token) return

        async function fetchProfile() {
            const res = await fetch(`${API_BASE_URL}/users/profile/me`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            })
            const json = await res.json()
            const raw = json.data

            setUser({
                id: raw.id,
                name: raw.name,
                username: raw.username,
                avatar: raw.avatarUrl,
                coverImage: raw.coverImageUrl,
                bio: raw.bio,
                location: raw.location || "",
                joinDate: raw.createdAt,
                followers: raw.totalFollowers || 0,
                following: raw.totalFollowing || 0,
                posts: raw.totalPosts || 0,
                isOwnProfile: String(raw.id) === userId,
            })
        }

        async function fetchPosts() {
            const res = await fetch(`${API_BASE_URL}/posts/user/${userId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            })
            const json = await res.json()
            setPosts(json.data || [])
        }

        fetchProfile().catch(console.error)
        fetchPosts().catch(console.error)
    }, [])

    if (!user) return <p>Loading...</p>

    return (
        <div className="min-h-screen bg-background">
            <UserNavbar/>
            <div className="container mx-auto px-4 py-6">
                <div className="max-w-4xl mx-auto space-y-6">
                    {/* Truyền user props xuống ProfileCard */}
                    <ProfileCard user={user}/>

                    <div className="max-w-2xl mx-auto space-y-6">
                        <h2 className="text-xl font-semibold">Bài viết của bạn</h2>
                        {posts.map((post) => (
                            <PostCard key={post.id} post={post}/>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    )
}

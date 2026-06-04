import { useEffect, useRef, useState } from "react";
import Header from "../Components/Header";
import { config } from "../config";
import { useLocation } from "react-router-dom";

type UserInfo = {
    id: string;
    username: string;
    email: string;
    userRole: string;
    createdAt: string;
};

type ApiResponse = {
    content: UserInfo[];
};

function Users() {

    const token = localStorage.getItem("token");

    const [users, setUsers] = useState<UserInfo[]>([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);

    const observerRef = useRef<HTMLDivElement>(null);
    
    const fetchingRef = useRef(false);
    const location = useLocation();

    const [resetKey, setResetKey] = useState(0);
    
        useEffect(() => {
            setUsers([]);
            setPage(0);
            setHasMore(true);
            fetchingRef.current = false;
            setResetKey(k => k + 1);
        }, [location.pathname]);

    useEffect(() => {
        async function loadUsers() {
            if (!hasMore || fetchingRef.current) return;

            fetchingRef.current = true;
            setLoading(true);

            try {
                const response = await fetch(
                    `${config.apiUrl}/api/users?page=${page}&size=18`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "application/json",
                        },
                    }
                );

                const data: ApiResponse = await response.json();

                setUsers(prev => {
                    const existingIds = new Set(prev.map(r => r.id));

                    const filtered = data.content.filter(
                        r => !existingIds.has(r.id)
                    );

                    return [...prev, ...filtered];
                });

                if (data.content.length < 18) {
                    setHasMore(false);
                }

            } finally {
                fetchingRef.current = false;
                setLoading(false);
            }
        }

        loadUsers();
    }, [page, resetKey]);

    useEffect(() => {
        const observer = new IntersectionObserver(
            entries => {
                if (
                    entries[0].isIntersecting &&
                    hasMore &&
                    !fetchingRef.current
                ) {
                    setPage(prev => prev + 1);
                }
            },
            {
                rootMargin: "300px",
                threshold: 0,
            }
        );

        const current = observerRef.current;

        if (current) {
            observer.observe(current);
        }

        return () => {
            if (current) {
                observer.unobserve(current);
            }
        };

    }, [hasMore]);

    async function handleChangeRole(role: string, id: string) {

        try {
            const response = await fetch(`${config.apiUrl}/api/users/${id}/role`, {
                method: "PATCH",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    role: role
                }),
            });

            if (!response.ok) {
                const text = await response.text();
                alert(`${text}`);
                return;
            }

            setUsers(prev =>
                prev.map(user =>
                    user.id === id
                        ? { ...user, userRole: role }
                        : user
                )
            );

        } catch (err) {
            alert(`Network error: ${String(err)}`);
        }
    }

    return(
        <>
            <Header />

            <div className="mx-auto px-6 py-6">
                {/* In page header */}
                <div className="flex justify-between items-center">
                    <h3>Users</h3>

                </div>


                {/* Lists cards */}
                <div className="mt-4 flex flex-wrap gap-2">
                    {users.map(user => (
                        <div
                            key={user.id}
                            className="flex items-center justify-between w-full px-3 py-4 bg-white rounded-md border border-[#a3a3a3] text-left text-m"
                        >
                            <div className="flex items-center gap-2">
                                <div className="font-semibold">
                                    {user.username}
                                </div>
                                -
                                <div>
                                    {user.email}
                                </div>
                            </div>
                            
                            <div>
                                {user.userRole === "USER" ? (
                                    <button
                                        className="rounded-md border border-[#a3a3a3] p-2"
                                        onClick={() => handleChangeRole("ADMIN", user.id)}
                                    >
                                        Make Admin
                                    </button>
                                ) : (
                                    <button
                                        className="rounded-md border border-[#a3a3a3] p-2"
                                        onClick={() => handleChangeRole("USER", user.id)}
                                    >
                                        Remove Admin
                                    </button>
                                )}
                                
                            </div>
                        </div>
                    ))}
                </div>

                <div ref={observerRef} className="h-10" />
                
                {loading && (
                    <p className="text-center py-4">
                        Loading more users...
                    </p>
                )}

            </div>
            
        </>
    );
}

export default Users;
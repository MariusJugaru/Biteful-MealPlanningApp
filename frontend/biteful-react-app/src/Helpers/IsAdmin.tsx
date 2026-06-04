import { jwtDecode } from "jwt-decode";

type JwtPayload = {
    sub: string;
    username: string;
    role: string;
    exp: number;
};

function isAdmin(token: string | null): boolean {
    if (!token) return false;

    try {
        const decoded = jwtDecode<JwtPayload>(token);

        const expired = decoded.exp * 1000 < Date.now();

        if (expired) return false;

        return decoded.role === "ADMIN";
    } catch {
        return false;
    }
}

export default isAdmin;
import { Cookies } from "react-cookie";

const cookies = new Cookies();

const isHttps = window.location.protocol === "https:";

export const setCookie = (name: string, value: string, days: number) => {
    const expires = new Date();

    expires.setUTCDate(expires.getUTCDate() + days);

    return cookies.set(name, value, {
        path: "/",
        expires,
        sameSite: isHttps ? "none" : "lax",
        secure: isHttps,
    });
};

export const getCookie = (name: string) => {
    return cookies.get(name);
};

export const removeCookie = (name: string, path = "/") => {
    cookies.remove(name, {
        path,
        sameSite: isHttps ? "none" : "lax",
        secure: isHttps,
    });
};
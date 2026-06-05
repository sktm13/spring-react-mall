import axios from "axios";

// 환경변수
const API_SERVER_HOST = import.meta.env.VITE_API_SERVER;

const rest_api_key = import.meta.env.VITE_KAKAO_REST_API_KEY;
const redirect_uri = import.meta.env.VITE_KAKAO_REDIRECT_URI;

const auth_code_path = "https://kauth.kakao.com/oauth/authorize";

export const getKakaoLoginLink = () => {
    const kakaoURL =
        `${auth_code_path}?client_id=${rest_api_key}` +
        `&redirect_uri=${encodeURIComponent(redirect_uri)}` +
        `&response_type=code`;

    return kakaoURL;
};

export const getMemberWithCode = async (code: string) => {
    const res = await axios.get(
        `${API_SERVER_HOST}/api/member/kakao`,
        {
            params: { code }
        }
    );

    return res.data;
};
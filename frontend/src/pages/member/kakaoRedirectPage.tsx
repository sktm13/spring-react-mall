import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router";
import { getMemberWithCode } from "../../api/kakaoApi";
import type { AppDispatch } from "../../store";
import { useDispatch } from "react-redux";
import { save } from "../../slices/loginSlice";

const KakaoRedirectPage = () => {

    const [searchParams] = useSearchParams();

    const authCode = searchParams.get("code");

    const dispatch = useDispatch<AppDispatch>();

    const navigate = useNavigate();

    useEffect(() => {

        if (!authCode) {
            return;
        }

        const handleKakaoLogin = async () => {
            try {
                const result = await getMemberWithCode(authCode);

                dispatch(save(result));

                if (result.social === true) {
                    navigate("/member/modify");
                } else {
                    navigate("/");
                }

            } catch (error) {
                console.error("Kakao login error:", error);
                navigate("/member/login");
            }
        };

        handleKakaoLogin();

    }, [authCode, dispatch, navigate]);

    return <div>카카오 로그인 처리 중...</div>;
};

export default KakaoRedirectPage;
import { Link } from "react-router";
import { getKakaoLoginLink } from "../../api/kakaoApi";
import { FaComment } from "react-icons/fa";

const KakaoLoginComponent = () => {

    const link = getKakaoLoginLink()

    return (
        <div className="flex flex-col items-center gap-3">

            {/* 안내 문구 */}
            <div className="text-sm text-gray-400">
                로그인 시 자동 가입됩니다
            </div>

            {/* 카카오 버튼 */}
            <Link to={link}>
                <div
                    className="w-12 h-12 flex items-center justify-center 
                               bg-yellow-400 rounded-full shadow-md
                               hover:scale-110 hover:shadow-lg transition"
                >
                    <FaComment className="text-black text-xl" />
                </div>
            </Link>

        </div>
    )
}

export default KakaoLoginComponent;
import { useNavigate } from "react-router"
import useCustomLogin from "../../hooks/useCustomLogin"

function LogoutComponent() {

    const { doLogout } = useCustomLogin()
    const navigate = useNavigate()

    const handleLogout = () => {
        doLogout()
        navigate("/")   //홈
    }
    return (
        <div className="w-full max-w-md bg-white rounded-2xl shadow-lg p-8 text-center">

            {/* 타이틀 */}
            <h2 className="text-2xl font-bold mb-4">
                로그아웃
            </h2>

            {/* 안내 문구 */}
            <p className="text-gray-600 mb-6">
                로그아웃 하시겠습니까?
            </p>

            {/* 버튼 영역 */}
            <div className="flex gap-3 justify-center">

                {/* 취소 */}
                <button
                    className="px-4 py-2 border rounded-lg text-gray-600 hover:bg-gray-100 transition"
                    onClick={() => navigate(-1)}
                >
                    취소
                </button>

                {/* 로그아웃 */}
                <button
                    className="px-4 py-2 bg-black text-white rounded-lg font-semibold hover:bg-gray-800 transition"
                    onClick={() => handleLogout()}
                >
                    로그아웃
                </button>

            </div>

        </div>
    )
}
export default LogoutComponent 

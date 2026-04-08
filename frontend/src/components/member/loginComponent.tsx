import { useState } from "react"
import ResultModal from "../common/resultModal";
import useCustomLogin from "../../hooks/useCustomLogin";
import KakaoLoginComponent from "./kakaoLoginComponent";

function LoginComponent() {

    const { doLogin, loginStatus, moveToPath } = useCustomLogin()
    const [email, setEmail] = useState('')
    const [pw, setPw] = useState('')
    const handleLogin = () => { doLogin(email, pw) }
    const closeModal = () => { moveToPath('/') }

    return (
        <div className="w-full max-w-md bg-white rounded-2xl shadow-lg p-8">

            {loginStatus === 'pending' &&
                <div className="mb-4 text-center text-sm text-gray-500">
                    로그인 중...
                </div>}

            {loginStatus === 'fulfilled' &&
                <ResultModal title="Login Result" content="로그인 되었습니다." callbackFn={closeModal} />}

            {loginStatus === 'rejected' &&
                <ResultModal title="Login Result" content="로그인에 실패하였습니다." callbackFn={closeModal} />}

            {/* 타이틀 */}
            <div className="flex justify-center">
                <div className="text-2xl font-bold mb-6">
                    로그인
                </div>
            </div>

            {/* Email */}
            <div className="flex justify-center">
                <div className="w-full mb-4">
                    <div className="text-sm font-semibold mb-1">Email</div>
                    <input
                        className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                        name="email"
                        type="text"
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
            </div>

            {/* Password */}
            <div className="flex justify-center">
                <div className="w-full mb-6">
                    <div className="text-sm font-semibold mb-1">Password</div>
                    <input
                        className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                        name="pw"
                        type="password"
                        onChange={(e) => setPw(e.target.value)}
                    />
                </div>
            </div>

            {/* 버튼 */}
            <div className="flex justify-center">
                <div className="w-full">
                    <button
                        type="submit"
                        className="w-full py-2 bg-black text-white rounded-lg font-semibold hover:bg-gray-800 transition"
                        onClick={() => handleLogin()}
                    >
                        LOGIN
                    </button>
                </div>
            </div>

            {/* 카카오 로그인 */}
            <div className="mt-6 flex flex-col items-center gap-2">
                <p className="text-sm text-gray-400">또는</p>
                <KakaoLoginComponent />
            </div>

        </div>
    )
} export default LoginComponent
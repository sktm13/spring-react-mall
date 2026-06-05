import { useActionState } from "react";
import { useNavigate } from "react-router";
import { useDispatch } from "react-redux";
import useCustomLogin from "../../hooks/useCustomLogin";
import jwtAxios from "../../util/jwtUtil";
import PendingModal from "../common/pendingModal";
import ResultModal from "../common/resultModal";
import { save } from "../../slices/loginSlice";
import type { AppDispatch } from "../../store";

interface ModifyResult {
    result: string;
    error: string;
    nickname?: string;
}

const initState: ModifyResult = {
    result: "",
    error: "",
};

const modifyAction = async (_state: ModifyResult, formData: FormData): Promise<ModifyResult> => {
    const email = formData.get("email") as string;
    const pw = formData.get("pw") as string;
    const nickname = formData.get("nickname") as string;

    if (pw.length < 8) {
        return {
            result: "",
            error: "패스워드는 8자 이상이어야 합니다.",
        };
    }

    try {
        await jwtAxios.put("/api/member/modify", {
            email,
            pw,
            nickname,
        });

    } catch (err: any) {
        return {
            result: "",
            error: err.response?.data?.message || "수정 중 오류가 발생했습니다.",
        };
    }

    return {
        result: "Modified",
        error: "",
        nickname,
    };
};

function ModifyComponent() {

    const { loginState } = useCustomLogin();

    const dispatch = useDispatch<AppDispatch>();

    const navigate = useNavigate();

    const [state, action, isPending] = useActionState(modifyAction, initState);

    const closeModal = () => {
        dispatch(save({
            ...loginState,
            nickname: state.nickname || loginState.nickname,
            social: false,
        }));

        navigate("/");
    };

    return (
        <div className="w-full max-w-md bg-white rounded-2xl shadow-lg p-8">

            {isPending && <PendingModal />}

            {state.error && (
                <div className="mb-4 text-sm text-red-500 text-center">
                    {state.error}
                </div>
            )}

            {state.result && (
                <ResultModal
                    title="회원 정보 수정"
                    content="회원 정보가 수정되었습니다."
                    callbackFn={closeModal}
                />
            )}

            <h2 className="text-2xl font-bold text-center mb-6">
                회원 정보 수정
            </h2>

            <form action={action} className="space-y-4">

                <div>
                    <label className="block text-sm font-medium mb-1">
                        Email
                    </label>
                    <input
                        className="w-full px-4 py-2 border rounded-lg bg-gray-100 text-gray-500"
                        name="email"
                        type="text"
                        defaultValue={loginState.email}
                        readOnly
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">
                        Password
                    </label>
                    <input
                        className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                        name="pw"
                        type="password"
                        placeholder="8자 이상 입력"
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium mb-1">
                        Nickname
                    </label>
                    <input
                        className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                        name="nickname"
                        type="text"
                        defaultValue={loginState.nickname}
                    />
                </div>

                <div className="flex gap-3 pt-2">

                    <button
                        type="button"
                        onClick={() => window.history.back()}
                        className="w-1/2 py-2 border rounded-lg text-gray-600 hover:bg-gray-100 transition"
                    >
                        취소
                    </button>

                    <button
                        type="submit"
                        className="w-1/2 py-2 bg-black text-white rounded-lg font-semibold hover:bg-gray-800 transition"
                    >
                        수정하기
                    </button>

                </div>

            </form>

        </div>
    );
}

export default ModifyComponent;
import { useEffect, useState } from "react";
import { getOne, putReply } from "../../api/inquiryApi";
import useCustomMove from "../../hooks/useCustomMove";
import useCustomLogin from "../../hooks/useCustomLogin";

function ReadComponent({ ino }: { ino: number }) {

  const [inquiry, setInquiry] = useState<Inquiry | undefined>();
  const [replyText, setReplyText] = useState("");

  const { moveToList, moveToModify } = useCustomMove();
  const { loginState } = useCustomLogin();
  console.log("loginState:", loginState)
  useEffect(() => {
    getOne(ino).then((data) => {
      setInquiry(data);
    });
  }, [ino]);

  if (!inquiry) {
    return (
      <div className="text-center py-20 text-gray-400">
        Loading...
      </div>
    );
  }

  console.log("roleNames:", loginState.roleNames);
  const isAdmin = loginState.roleNames.includes("ADMIN");

  const handleReply = () => {
    putReply(ino, { reply: replyText }).then(() => {
      alert("답변 완료");
      window.location.reload(); // 간단 처리
    });
  };

  return (
    <div className="bg-white rounded-2xl shadow border border-gray-200 p-6">

      {/* 제목 */}
      <div className="mb-6">
        <div className="text-sm text-gray-400 mb-1">Title</div>
        <div className="text-2xl font-bold">{inquiry.title}</div>
      </div>

      {/* 내용 */}
      <div className="mb-6">
        <div className="text-sm text-gray-400 mb-1">Content</div>
        <div className="text-lg">{inquiry.content}</div>
      </div>

      {/* 정보 */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 border-t pt-6">

        <InfoItem label="Inquiry No" value={inquiry.ino} />
        <InfoItem label="Writer" value={inquiry.writer} />

        <InfoItem label="Created Date" value={inquiry.createdDate.substring(0, 10)} />

        <div className="sm:col-span-2">
          <InfoItem
            label="Status"
            value={
              <span
                className={`px-3 py-1 rounded-full text-sm font-semibold ${inquiry.status === "DONE"
                    ? "bg-green-100 text-green-600"
                    : "bg-gray-200 text-gray-600"
                  }`}
              >
                {inquiry.status}
              </span>
            }
          />
        </div>

      </div>

      {/* 답변 표시 */}
      {inquiry.reply && (
        <div className="mt-8 p-4 border rounded-lg bg-gray-50">
          <div className="text-sm text-gray-400 mb-2">Reply</div>
          <div className="text-lg">{inquiry.reply}</div>
        </div>
      )}

      {/* 관리자 답변 입력 */}
      {isAdmin && !inquiry.reply && (
        <div className="mt-8">
          <textarea
            className="w-full border rounded-lg p-3"
            rows={4}
            placeholder="답변을 입력하세요..."
            value={replyText}
            onChange={(e) => setReplyText(e.target.value)}
          />

          <button
            className="mt-3 px-5 py-2 rounded-lg bg-green-500 text-white hover:bg-green-600"
            onClick={handleReply}
          >
            답변 등록
          </button>
        </div>
      )}

      {/* 버튼 */}
      <div className="flex justify-end gap-3 mt-8">

        <button
          className="px-5 py-3 rounded-xl bg-gray-500 text-white hover:bg-gray-600 transition"
          onClick={() => moveToList()}
        >
          List
        </button>

        <button
          className="px-5 py-3 rounded-xl bg-blue-500 text-white hover:bg-blue-600 transition"
          onClick={() => moveToModify(ino)}
        >
          Modify
        </button>

      </div>
    </div>
  );
}

export default ReadComponent;



const InfoItem = ({
  label,
  value,
}: {
  label: string;
  value: React.ReactNode;
}) => (
  <div>
    <div className="text-sm text-gray-400 mb-1">{label}</div>
    <div className="text-lg font-semibold">{value}</div>
  </div>
);
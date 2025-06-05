// App.js
import React, { useState } from 'react';
import './App.css';

function App() {
  const [question, setQuestion] = useState('');
  const [userId, setUserId] = useState(''); // userId 상태 추가
  const [answer, setAnswer] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!userId.trim()) { // userId 유효성 검사 추가
      setError('사용자 ID를 입력해주세요.');
      return;
    }
    if (!question.trim()) {
      setError('질문을 입력해주세요.');
      return;
    }

    setIsLoading(true);
    setAnswer('');
    setError('');

    try {
      const response = await fetch('http://localhost:8000/search', { // 테스트용 엔드포인트
        method: 'POST',
        headers: {
          'Content-Type': 'text/plain', // JSON으로 변경
        },
        body: question, // userId와 question 함께 전송
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detail || `서버 오류: ${response.status}`);
      }

      const data = await response.json();
      setAnswer(data.answer);

    } catch (err) {
      console.error("API 요청 중 오류 발생:", err);
      setError(err.message || '답변을 가져오는 중 오류가 발생했습니다.');
      setAnswer('');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>청년 정책 알리미 (메모리 테스트)</h1>
      </header>
      <main>
        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="userIdInput">사용자 ID:</label>
            <input
              type="text"
              id="userIdInput"
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
              placeholder="테스트용 사용자 ID 입력..."
              disabled={isLoading}
            />
          </div>
          <div>
            <label htmlFor="questionInput">질문 입력:</label>
            <textarea
              id="questionInput"
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              placeholder="청년 정책에 대해 질문해주세요..."
              rows="4"
              cols="50"
              disabled={isLoading}
            />
          </div>
          <button type="submit" disabled={isLoading}>
            {isLoading ? '질문 중...' : '질문하기'}
          </button>
        </form>

        {error && (
          <div style={{ color: 'red', marginTop: '20px' }}>
            <p>오류: {error}</p>
          </div>
        )}

        {/* ***** 여기가 answer 상태를 화면에 표시하는 부분입니다 ***** */}
        {answer && (
          <div style={{ marginTop: '20px', border: '1px solid #ccc', padding: '10px' }}>
            <h2>답변:</h2>
            {/* 답변 내용에 줄바꿈이 있다면 이를 유지하기 위해 whiteSpace: 'pre-wrap' 사용 */}
            <p style={{ whiteSpace: 'pre-wrap' }}>{answer}</p>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
// app.js
import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [question, setQuestion] = useState('');
  const [answer, setAnswer] = useState('');
  const [sources, setSources] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [userId, setUserId] = useState('');

  useEffect(() => {
    let currentUserId = localStorage.getItem('ragAppUserId');
    if (!currentUserId) {
      currentUserId = `user_${Date.now()}_${Math.random().toString(36).substring(2, 7)}`;
      localStorage.setItem('ragAppUserId', currentUserId);
    }
    setUserId(currentUserId);
    console.log("Current User ID:", currentUserId);
  }, []);


  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!userId) { // userId가 설정되었는지 먼저 확인
        setError('사용자 ID가 준비되지 않았습니다. 잠시 후 다시 시도해주세요.');
        setIsLoading(false); // 로딩 상태 해제
        return;
    }

    if (!question.trim()) {
      setError('질문을 입력해주세요.');
      return;
    }

    setIsLoading(true);
    setAnswer('');
    setSources([]);
    setError('');

    try {
      const requestBody = {
        user_id: userId,
        question: question,
      };

      const response = await fetch('http://localhost:8000/search', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      });

      if (!response.ok) {
        let errorDetail = `서버 오류: ${response.status}`;
        try {
            const errorData = await response.json();
            errorDetail = errorData.detail || errorDetail;
        } catch (e) { /* JSON 파싱 실패 시 무시 */ }
        throw new Error(errorDetail);
      }

      const data = await response.json();
      setAnswer(data.answer);
      setSources(data.sources || []);

    } catch (err) {
      console.error("API 요청 중 오류 발생:", err);
      setError(err.message || '답변을 가져오는 중 오류가 발생했습니다.');
      setAnswer('');
      setSources([]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleClearHistory = async () => {
    if (!userId) { // userId가 설정되었는지 먼저 확인
        setError('사용자 ID가 준비되지 않았습니다. 잠시 후 다시 시도해주세요.');
        setIsLoading(false); // 로딩 상태 해제
        return;
    }

    setIsLoading(true);
    setError('');
    try {
        const response = await fetch('http://localhost:8000/clear_history', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ user_id: userId }),
        });
        if (!response.ok) {
            let errorDetail = `서버 오류: ${response.status}`;
            try {
                const errorData = await response.json();
                errorDetail = errorData.detail || errorDetail;
            } catch(e) {/* JSON 파싱 실패 시 무시 */}
            throw new Error(errorDetail);
        }
        alert('대화 기록이 삭제되었습니다.');
        setAnswer('');
        setSources([]);
        setQuestion('');
    } catch (err) {
        console.error("대화 기록 삭제 중 오류:", err);
        setError(err.message || '대화 기록 삭제 중 오류가 발생했습니다.');
    } finally {
        setIsLoading(false);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>청년 정책 알리미</h1>
        {userId && <p style={{fontSize: '0.8em', color: '#aaa'}}>사용자 ID: {userId}</p>}
      </header>
      <main>
        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="questionInput">질문 입력:</label>
            <textarea
              id="questionInput"
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              placeholder="청년 정책에 대해 질문해주세요..."
              rows="4"
              cols="50"
              disabled={isLoading || !userId} // userId가 없으면 입력 비활성화
            />
          </div>
          <button type="submit" disabled={isLoading || !userId}> {/* userId가 없으면 버튼 비활성화 */}
            {isLoading ? '질문 중...' : '질문하기'}
          </button>
          <button type="button" onClick={handleClearHistory} disabled={isLoading || !userId} style={{marginLeft: '10px'}}> {/* userId가 없으면 버튼 비활성화 */}
            대화 기록 삭제
          </button>
        </form>

        {error && (
          <div style={{ color: 'red', marginTop: '20px' }}>
            <p>오류: {error}</p>
          </div>
        )}

        {answer && (
          <div style={{ marginTop: '20px', border: '1px solid #ccc', padding: '10px' }}>
            <h2>답변:</h2>
            <p style={{ whiteSpace: 'pre-wrap' }}>{answer}</p>
          </div>
        )}

        {sources.length > 0 && (
          <div style={{ marginTop: '20px', border: '1px solid #eee', padding: '10px' }}>
            <h3>LLM에 전달된 컨텍스트 (검색된 문서):</h3>
            {sources.map((source, index) => (
              <div key={index} style={{ marginBottom: '10px', padding: '5px', border: '1px dashed #ddd', whiteSpace: 'pre-wrap', maxHeight: '150px', overflowY: 'auto', fontSize: '0.9em' }}>
                <strong>문서 {index + 1}:</strong>
                <p>{source}</p>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
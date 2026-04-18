import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Auth from './pages/Auth';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Auth />} />
        {/* We will add Dashboard, Admin, Profile, EventDetail, Help as we build them */}
        <Route path="*" element={<Auth />} />
      </Routes>
    </Router>
  );
}

export default App;

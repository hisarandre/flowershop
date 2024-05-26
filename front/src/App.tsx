import './App.css';

import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from './components/auth/LoginPage';
import UserService from './components/service/UserService';

function App() {

  return (
    <BrowserRouter>
      <div className="App">
        <div className="content">
          <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/login" element={<LoginPage />} />


            <Route path="*" element={<Navigate to="/login" />} />‰
          </Routes>
        </div>
      </div>
    </BrowserRouter>
  );
}

export default App;

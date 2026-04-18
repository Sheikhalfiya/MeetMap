import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Auth = () => {
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('login');
    const [showAdminModal, setShowAdminModal] = useState(false);
    const [customInterest, setCustomInterest] = useState(false);

    // Form states
    const [loginEmail, setLoginEmail] = useState('');
    const [loginPassword, setLoginPassword] = useState('');
    const [loginError, setLoginError] = useState('');

    const [signupData, setSignupData] = useState({
        name: '', email: '', password: '', city: '',
        dateOfBirth: '', phoneNumber: '', ageGroup: '',
        interests: [], customInterestsText: ''
    });
    const [signupError, setSignupError] = useState('');

    const [adminEmail, setAdminEmail] = useState('');
    const [adminPassword, setAdminPassword] = useState('');
    const [adminError, setAdminError] = useState('');

    useEffect(() => {
        if (localStorage.getItem('user')) {
            navigate('/dashboard');
        }
        if (localStorage.getItem('admin')) {
            navigate('/admin');
        }
    }, [navigate]);

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoginError('');
        try {
            const res = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: loginEmail, password: loginPassword })
            });
            const data = await res.json();
            if (res.ok) {
                localStorage.setItem('user', JSON.stringify(data));
                navigate('/dashboard');
            } else {
                setLoginError(data.message || 'Login failed');
            }
        } catch (err) {
            setLoginError('Network error');
        }
    };

    const handleSignupChange = (e) => {
        const { name, value, type, checked } = e.target;
        if (type === 'checkbox') {
            if (value === 'Custom') {
                setCustomInterest(checked);
            } else {
                setSignupData(prev => ({
                    ...prev,
                    interests: checked 
                        ? [...prev.interests, value]
                        : prev.interests.filter(i => i !== value)
                }));
            }
        } else {
            setSignupData(prev => ({ ...prev, [name]: value }));
        }
    };

    const handleSignup = async (e) => {
        e.preventDefault();
        setSignupError('');
        
        let areaOfInterestArr = [...signupData.interests];
        if (customInterest && signupData.customInterestsText.trim() !== '') {
            areaOfInterestArr.push(signupData.customInterestsText.trim());
        }
        const areaOfInterest = areaOfInterestArr.join(', ');

        const payload = {
            name: signupData.name,
            email: signupData.email,
            password: signupData.password,
            city: signupData.city,
            dateOfBirth: signupData.dateOfBirth,
            phoneNumber: signupData.phoneNumber,
            ageGroup: signupData.ageGroup,
            areaOfInterest
        };

        try {
            const res = await fetch('/api/auth/signup', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            const data = await res.json();
            if (res.ok) {
                alert('Account created! Please log in.');
                setActiveTab('login');
                setLoginEmail(signupData.email);
            } else {
                setSignupError(data.message || 'Signup failed');
            }
        } catch (err) {
            setSignupError('Network error');
        }
    };

    const handleAdminLogin = async (e) => {
        e.preventDefault();
        setAdminError('');
        try {
            const res = await fetch('/api/auth/admin/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: adminEmail, password: adminPassword })
            });
            const data = await res.json();
            if (res.ok) {
                localStorage.setItem('admin', JSON.stringify(data));
                navigate('/admin');
            } else {
                setAdminError(data.message || 'Invalid admin credentials');
            }
        } catch (err) {
            setAdminError('Network error');
        }
    };

    return (
        <div style={{ background: "url('https://plus.unsplash.com/premium_vector-1741548994291-c8c6e8e0cac7?q=80&w=1228&auto=format&fit=crop') no-repeat center center fixed", backgroundSize: 'cover', minHeight: '100vh', color: 'var(--text-main)', fontFamily: "'Inter', sans-serif" }}>
            <nav className="navbar" style={{ background: 'rgba(255, 255, 255, 0.4)', backdropFilter: 'blur(20px)' }}>
                <div className="container nav-container">
                    <a href="#" className="logo" onClick={(e) => {e.preventDefault(); navigate('/');}}>
                        <i className="fa-solid fa-map-location-dot"></i> MeetMap
                    </a>
                    <div className="nav-links">
                        <button className="btn btn-outline" onClick={() => setShowAdminModal(true)}>Admin</button>
                    </div>
                </div>
            </nav>

            <div className="auth-wrapper container">
                <div className="card auth-container" style={{ background: 'rgba(255, 255, 255, 0.5)', backdropFilter: 'blur(25px)', border: '1px solid rgba(255, 255, 255, 0.6)', boxShadow: '0 8px 32px rgba(0, 0, 0, 0.1)' }}>
                    <div className="auth-tabs">
                        <div className={`auth-tab ${activeTab === 'login' ? 'active' : ''}`} onClick={() => setActiveTab('login')}>Login</div>
                        <div className={`auth-tab ${activeTab === 'signup' ? 'active' : ''}`} onClick={() => setActiveTab('signup')}>Sign Up</div>
                    </div>

                    {activeTab === 'login' && (
                        <form onSubmit={handleLogin}>
                            <div className="form-group">
                                <label>Email</label>
                                <input type="email" className="form-control" required placeholder="your@email.com" value={loginEmail} onChange={e => setLoginEmail(e.target.value)} />
                            </div>
                            <div className="form-group">
                                <label>Password</label>
                                <input type="password" className="form-control" required placeholder="********" value={loginPassword} onChange={e => setLoginPassword(e.target.value)} />
                            </div>
                            <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>Login</button>
                            {loginError && <div className="mt-3 text-center" style={{ color: '#ef4444' }}>{loginError}</div>}
                        </form>
                    )}

                    {activeTab === 'signup' && (
                        <form onSubmit={handleSignup}>
                            <div className="form-group">
                                <label>Full Name</label>
                                <input type="text" name="name" className="form-control" required placeholder="John Doe" onChange={handleSignupChange} />
                            </div>
                            <div className="form-group">
                                <label>Email</label>
                                <input type="email" name="email" className="form-control" required placeholder="your@email.com" onChange={handleSignupChange} />
                            </div>
                            <div className="form-group">
                                <label>Password</label>
                                <input type="password" name="password" className="form-control" required placeholder="********" onChange={handleSignupChange} />
                            </div>
                            <div className="form-group">
                                <label>City (Location)</label>
                                <input type="text" name="city" className="form-control" required placeholder="New York" onChange={handleSignupChange} />
                            </div>
                            <div className="form-group">
                                <label>Date of Birth</label>
                                <input type="date" name="dateOfBirth" className="form-control" required onChange={handleSignupChange} />
                            </div>
                            <div className="form-group">
                                <label>Phone Number</label>
                                <input type="tel" name="phoneNumber" className="form-control" required placeholder="+1234567890" onChange={handleSignupChange} />
                            </div>
                            <div className="form-group">
                                <label>Age Group</label>
                                <select name="ageGroup" className="form-control" required onChange={handleSignupChange}>
                                    <option value="">Select Age Group</option>
                                    <option value="Under 18">Under 18</option>
                                    <option value="18-25">18-25</option>
                                    <option value="26-35">26-35</option>
                                    <option value="36-50">36-50</option>
                                    <option value="51+">51+</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Areas of Interest</label>
                                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '8px', marginTop: '5px' }}>
                                    <label><input type="checkbox" name="interest" value="Creative" onChange={handleSignupChange} /> Creative</label>
                                    <label><input type="checkbox" name="interest" value="Sports" onChange={handleSignupChange} /> Sports</label>
                                    <label><input type="checkbox" name="interest" value="Tech" onChange={handleSignupChange} /> Tech</label>
                                    <label><input type="checkbox" name="interest" value="Local Event" onChange={handleSignupChange} /> Local Event</label>
                                    <label><input type="checkbox" name="interest" value="Exhibition" onChange={handleSignupChange} /> Exhibition</label>
                                    <label><input type="checkbox" name="interest" value="Gathering" onChange={handleSignupChange} /> Gathering</label>
                                    <label><input type="checkbox" name="interest" value="Custom" onChange={handleSignupChange} /> Custom...</label>
                                </div>
                                {customInterest && (
                                    <input type="text" name="customInterestsText" className="form-control mt-2" placeholder="Enter custom interest" style={{ width: '100%' }} onChange={handleSignupChange} required />
                                )}
                            </div>
                            <button type="submit" className="btn btn-accent" style={{ width: '100%' }}>Create Account</button>
                            {signupError && <div className="mt-3 text-center" style={{ color: '#ef4444' }}>{signupError}</div>}
                        </form>
                    )}
                </div>
            </div>

            {showAdminModal && (
                <div className="modal active">
                    <div className="modal-content" style={{ maxWidth: '400px' }}>
                        <button className="close-modal" onClick={() => setShowAdminModal(false)}>&times;</button>
                        <h2 className="mb-4">Admin Access</h2>
                        <form onSubmit={handleAdminLogin}>
                            <div className="form-group text-left">
                                <label>Admin Email</label>
                                <input type="email" className="form-control" required placeholder="admin@example.com" value={adminEmail} onChange={e => setAdminEmail(e.target.value)} />
                            </div>
                            <div className="form-group text-left">
                                <label>Password</label>
                                <input type="password" className="form-control" required placeholder="********" value={adminPassword} onChange={e => setAdminPassword(e.target.value)} />
                            </div>
                            <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>Login as Admin</button>
                            {adminError && <div className="mt-3 text-center" style={{ color: '#ef4444' }}>{adminError}</div>}
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Auth;

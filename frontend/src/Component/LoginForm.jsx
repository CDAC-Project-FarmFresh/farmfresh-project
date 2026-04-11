import { useEffect, useState } from 'react';
import adminService from '../Services/admin.services';
import { Link, useNavigate } from 'react-router-dom';
import toast, { Toaster } from 'react-hot-toast';
import './LoginForm.css';
import { clearAuth, persistAuth } from '../auth/storage';

function LoginForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    clearAuth();
  }, []);

  const loginUser = (e) => {
    e.preventDefault();
    const email = username.trim().toLowerCase();
    const user = { email, password };
    adminService
      .login(user)
      .then((response) => {
        const body = response.data;
        if (!body || !body.user || !body.accessToken) {
          toast.error('Invalid credentials', {
            style: { borderRadius: '10px', background: '#333', color: '#fff' },
          });
          return;
        }
        persistAuth(body);
        const u = body.user;
        if (u.isadmin === true) {
          navigate('/admin');
        } else {
          navigate('/user');
        }
      })
      .catch(() => {
        toast.error('Invalid credentials', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
      });
  };

  return (
    <div
      style={{
        backgroundImage:
          'url("https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=1600")',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center center',
      }}
    >
      <Toaster position="top-center" reverseOrder={false} toastOptions={{ duration: 2200 }} />
      <div className="container h-100">
        <div className="row justify-content-sm-center h-100">
          <div className="col-xxl-4 col-xl-5 col-lg-5 col-md-7 col-sm-9">
            <div className="text-center my-5">
              <h1 className="fw-bold text-success">Farm Fresh</h1>
            </div>
            <div className="card shadow-lg">
              <div className="card-body p-5">
                <h1 className="fs-4 card-title fw-bold mb-4">Login</h1>
                <form onSubmit={(e) => loginUser(e)}>
                  <div className="mb-3">
                    <label className="mb-2 text-muted" htmlFor="email">
                      E-Mail Address
                    </label>
                    <input
                      id="email"
                      type="email"
                      className="form-control"
                      name="email"
                      required
                      autoFocus
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                    />
                  </div>
                  <div className="mb-3">
                    <div className="mb-2 w-100">
                      <label className="text-muted" htmlFor="password">
                        Password
                      </label>
                    </div>
                    <input
                      id="password"
                      type="password"
                      className="form-control"
                      name="password"
                      required
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                    />
                  </div>
                  <div className="d-flex align-items-center flex-wrap gap-2">
                    <button type="submit" className="btn btn-primary">
                      Login
                    </button>
                    <Link to="/forgot-password" className="small text-decoration-none ms-sm-auto">
                      Forgot password?
                    </Link>
                  </div>
                </form>
              </div>
              <div className="card-footer py-1 border-0">
                <div className="text-center mb-3">
                  New user? <a href="/register" className="text-dark">Register</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginForm;

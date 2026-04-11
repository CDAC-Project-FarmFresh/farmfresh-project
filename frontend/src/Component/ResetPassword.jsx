import { useEffect, useMemo, useState } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import toast from 'react-hot-toast';
import { resetPassword } from '../Services/auth.services';

function ResetPassword() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const tokenFromUrl = useMemo(() => searchParams.get('token') || '', [searchParams]);
  const [token, setToken] = useState(tokenFromUrl);
  const [password, setPassword] = useState('');
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (tokenFromUrl) {
      setToken(tokenFromUrl);
    }
  }, [tokenFromUrl]);

  const onSubmit = (e) => {
    e.preventDefault();
    setSubmitting(true);
    resetPassword(token.trim(), password)
      .then((res) => {
        toast.success(res.data?.message || 'Password updated.', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
        setTimeout(() => navigate('/signup'), 1200);
      })
      .catch((err) => {
        toast.error(err.response?.data?.message || 'Reset failed. Link may be invalid or expired.', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
      })
      .finally(() => setSubmitting(false));
  };

  return (
    <div
      style={{
        backgroundImage:
          'url("https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=1600")',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center center',
        minHeight: '100vh',
      }}
    >
      <div className="container h-100 py-5">
        <div className="row justify-content-sm-center">
          <div className="col-xxl-4 col-xl-5 col-lg-5 col-md-7 col-sm-9">
            <div className="text-center my-4">
              <h1 className="fw-bold text-success">Farm Fresh</h1>
            </div>
            <div className="card shadow-lg">
              <div className="card-body p-5">
                <h1 className="fs-4 card-title fw-bold mb-2">Set a new password</h1>
                <p className="text-muted small mb-4">
                  Paste the token from your email if it is not already filled in, then choose a new password.
                </p>
                <form onSubmit={onSubmit}>
                  <div className="mb-3">
                    <label className="mb-2 text-muted" htmlFor="token">
                      Reset token
                    </label>
                    <input
                      id="token"
                      type="text"
                      className="form-control font-monospace small"
                      required
                      autoFocus={!tokenFromUrl}
                      value={token}
                      onChange={(e) => setToken(e.target.value)}
                    />
                  </div>
                  <div className="mb-3">
                    <label className="mb-2 text-muted" htmlFor="password">
                      New password
                    </label>
                    <input
                      id="password"
                      type="password"
                      className="form-control"
                      required
                      minLength={6}
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                    />
                  </div>
                  <button type="submit" className="btn btn-primary" disabled={submitting}>
                    {submitting ? 'Saving…' : 'Update password'}
                  </button>
                </form>
              </div>
              <div className="card-footer py-3 border-0 text-center">
                <Link to="/signup" className="text-dark">
                  Back to login
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ResetPassword;

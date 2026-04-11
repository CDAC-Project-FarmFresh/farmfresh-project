import { useState } from 'react';
import { Link } from 'react-router-dom';
import toast from 'react-hot-toast';
import { forgotPassword } from '../Services/auth.services';

function ForgotPassword() {
  const [email, setEmail] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const onSubmit = (e) => {
    e.preventDefault();
    setSubmitting(true);
    forgotPassword(email.trim().toLowerCase())
      .then((res) => {
        toast.success(res.data?.message || 'Check your email for reset instructions.', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
      })
      .catch((err) => {
        toast.error(err.response?.data?.message || 'Something went wrong.', {
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
                <h1 className="fs-4 card-title fw-bold mb-2">Forgot password</h1>
                <p className="text-muted small mb-4">
                  Enter the email you used to register. If it exists in our system, we will send reset instructions.
                </p>
                <form onSubmit={onSubmit}>
                  <div className="mb-3">
                    <label className="mb-2 text-muted" htmlFor="email">
                      E-Mail Address
                    </label>
                    <input
                      id="email"
                      type="email"
                      className="form-control"
                      required
                      autoFocus
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                    />
                  </div>
                  <button type="submit" className="btn btn-primary" disabled={submitting}>
                    {submitting ? 'Sending…' : 'Send reset link'}
                  </button>
                </form>
              </div>
              <div className="card-footer py-3 border-0 text-center">
                <Link to="/signup" className="text-dark me-3">
                  Back to login
                </Link>
                <Link to="/register" className="text-dark">
                  Register
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ForgotPassword;

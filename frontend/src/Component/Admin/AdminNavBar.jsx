import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { clearAuth, loadUser } from '../../auth/storage';

function AdminNavBar() {
  const [loading, setLoading] = useState(false);
  const [object, setObject] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    setObject(loadUser());
  }, []);

  const logout = () => {
    setLoading(true);
    setTimeout(() => {
      clearAuth();
      setLoading(false);
      navigate('/');
    }, 400);
  };

  return (
    <div>
      {!loading && (
        <nav className="navbar navbar-expand-lg py-0" style={{ backgroundColor: '#E0FFFF' }}>
          <div className="container">
            <a href="/admin" className="navbar-brand">
              <span className="fw-bold text-success">Farm Fresh</span>
            </a>
            <button
              type="button"
              className="navbar-toggler"
              data-bs-toggle="collapse"
              data-bs-target="#navbarCollapse"
            >
              <span className="navbar-toggler-icon" />
            </button>
            <div className="collapse navbar-collapse" id="navbarCollapse">
              <div className="navbar-nav">
                <a className="nav-item nav-link active fw-bolder text-black" style={{ letterSpacing: '4px' }}>
                  Hello, {object?.firstname}
                </a>
              </div>
              <div className="navbar-nav ms-auto">
                <a className="nav-item nav-link disabled fw-bolder text-black" style={{ letterSpacing: '4px' }}>
                  Admin Dashboard
                </a>
              </div>
              <div className="navbar-nav dropdown ms-auto">
                <a className="nav-link " href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="30"
                    height="30"
                    fill="currentColor"
                    className="bi bi-person-circle"
                    viewBox="0 0 16 16"
                  >
                    <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
                    <path
                      fillRule="evenodd"
                      d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"
                    />
                  </svg>
                </a>
                <ul className="dropdown-menu dropdown-menu-dark dropdown-menu-center">
                  <li>
                    <a className="dropdown-item text-center" href="/admin/profile">
                      Profile
                    </a>
                  </li>
                  <li>
                    <button type="button" className="dropdown-item text-center" onClick={logout}>
                      LogOut
                    </button>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </nav>
      )}
    </div>
  );
}

export default AdminNavBar;

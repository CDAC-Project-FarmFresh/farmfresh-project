import { Navigate, Outlet } from 'react-router-dom';
import { loadUser } from '../auth/storage';

const useAuth = () => {
  const user = loadUser();
  return Boolean(user && user.isadmin === true);
};

const AdminProtectedRoutes = () => {
  const auth = useAuth();
  return auth ? <Outlet /> : <Navigate to="/" />;
};

export default AdminProtectedRoutes;

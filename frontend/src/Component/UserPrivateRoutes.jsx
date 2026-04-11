import { Navigate, Outlet } from 'react-router-dom';
import { loadUser } from '../auth/storage';

const useAuth = () => {
  const user = loadUser();
  if (!user) {
    return false;
  }
  return user.isadmin !== true;
};

const UserProtectedRoutes = () => {
  const auth = useAuth();
  return auth ? <Outlet /> : <Navigate to="/" />;
};

export default UserProtectedRoutes;

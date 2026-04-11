export function loadAuth() {
  try {
    const raw = localStorage.getItem('farmfresh-auth');
    if (raw) {
      return JSON.parse(raw);
    }
  } catch {
    /* ignore */
  }
  return null;
}

export function loadUser() {
  return loadAuth()?.user ?? null;
}

export function persistAuth(authPayload) {
  localStorage.setItem('farmfresh-auth', JSON.stringify(authPayload));
}

export function clearAuth() {
  localStorage.removeItem('farmfresh-auth');
  localStorage.removeItem('user-token');
}

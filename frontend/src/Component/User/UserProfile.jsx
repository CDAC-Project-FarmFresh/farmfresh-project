import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import UserNavBar from './UserNavBar';
import userServices from '../../Services/user.services';
import toast from 'react-hot-toast';
import { loadUser, persistAuth } from '../../auth/storage';

function UserProfile() {
  const [isDisabled, setIsDisabled] = useState(true);
  const [email, setEmail] = useState('');
  const [firstname, setFirstname] = useState('');
  const [lastname, setLastname] = useState('');
  const [phoneNo, setPhoneNo] = useState('');
  const [address, setAddress] = useState('');
  const [phoneVerified, setPhoneVerified] = useState(false);
  const [otpCode, setOtpCode] = useState('');
  const [phoneBusy, setPhoneBusy] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    init();
  }, []);

  const mergeUserIntoAuth = (userDto) => {
    const auth = JSON.parse(localStorage.getItem('farmfresh-auth') || '{}');
    auth.user = { ...auth.user, ...userDto };
    persistAuth(auth);
  };

  const init = () => {
    userServices
      .getProfile()
      .then((response) => {
        const d = response.data;
        setAddress(d.address ?? '');
        setFirstname(d.firstname ?? '');
        setLastname(d.lastname ?? '');
        setPhoneNo(d.phoneNo ?? '');
        setEmail(d.email ?? '');
        setPhoneVerified(Boolean(d.phoneVerified));
      })
      .catch(() => {});
  };

  const updateProfile = () => {
    setIsDisabled(false);
  };

  const saveProfile = () => {
    const data = { email, firstname, lastname, phoneNo, address };
    userServices
      .updateProfile(data)
      .then((response) => {
        mergeUserIntoAuth(response.data);
        setPhoneVerified(Boolean(response.data.phoneVerified));
        toast.success('Profile updated', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
        setIsDisabled(true);
      })
      .catch((err) => {
        toast.error(err.response?.data?.message || 'Update failed', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
      });
  };

  const sendPhoneCode = () => {
    setPhoneBusy(true);
    userServices
      .requestPhoneVerificationCode()
      .then((res) => {
        toast.success(res.data?.message || 'Code sent to your email.', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
      })
      .catch((err) => {
        toast.error(err.response?.data?.message || 'Could not send code', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
      })
      .finally(() => setPhoneBusy(false));
  };

  const confirmPhoneCode = () => {
    const code = otpCode.trim();
    if (!/^\d{6}$/.test(code)) {
      toast.error('Enter the 6-digit code from your email.', {
        style: { borderRadius: '10px', background: '#333', color: '#fff' },
      });
      return;
    }
    setPhoneBusy(true);
    userServices
      .confirmPhoneVerification(code)
      .then((res) => {
        mergeUserIntoAuth(res.data);
        setPhoneVerified(Boolean(res.data.phoneVerified));
        setOtpCode('');
        toast.success('Phone number verified.', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
      })
      .catch((err) => {
        toast.error(err.response?.data?.message || 'Verification failed', {
          style: { borderRadius: '10px', background: '#333', color: '#fff' },
        });
      })
      .finally(() => setPhoneBusy(false));
  };

  const u = loadUser();

  return (
    <div style={{ backgroundColor: 'beige' }}>
      <UserNavBar />
      <div className="container h-100">
        <div className="row justify-content-sm-center h-100">
          <div className="col-xxl-6 col-xl-5 col-lg-5 col-md-7 col-sm-9">
            <div className="text-center my-4">
              <h2 className="text-success fw-bold">Farm Fresh</h2>
              {u?.userId != null && (
                <p className="text-muted small mb-0">Account #{u.userId}</p>
              )}
            </div>
            <div className="card shadow-lg mb-4">
              <div className="card-body px-5 pt-5">
                <h1 className="fs-4 card-title fw-bold mb-4">User Profile</h1>
                <div className="row g-3">
                  <div className="col mb-3">
                    <label className="mb-2 text-muted" htmlFor="firstname">
                      First Name
                    </label>
                    <input
                      id="firstname"
                      type="text"
                      className="form-control"
                      name="firstname"
                      required
                      value={firstname}
                      disabled={isDisabled}
                      onChange={(e) => setFirstname(e.target.value)}
                    />
                  </div>
                  <div className="col mb-3">
                    <label className="mb-2 text-muted" htmlFor="lastname">
                      Last Name
                    </label>
                    <input
                      id="lastname"
                      type="text"
                      className="form-control"
                      name="lastname"
                      required
                      value={lastname}
                      disabled={isDisabled}
                      onChange={(e) => setLastname(e.target.value)}
                    />
                  </div>
                </div>
                <div className="row g-3">
                  <div className="col mb-3">
                    <label className="mb-2 text-muted" htmlFor="email">
                      E-Mail Address
                    </label>
                    <input
                      id="email"
                      type="email"
                      className="form-control"
                      name="email"
                      required
                      pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
                      value={email}
                      disabled={isDisabled}
                      onChange={(e) => setEmail(e.target.value)}
                    />
                  </div>
                </div>
                <div className="row g-3">
                  <div className="col mb-3">
                    <label className="mb-2 text-muted" htmlFor="phone">
                      Phone No.
                    </label>
                    <input
                      id="phone"
                      type="tel"
                      className="form-control"
                      name="phone"
                      required
                      pattern="[0-9]{10}"
                      value={phoneNo}
                      disabled={isDisabled}
                      onChange={(e) => setPhoneNo(e.target.value)}
                    />
                    <div className="form-text">
                      Status:{' '}
                      {phoneVerified ? (
                        <span className="text-success fw-semibold">Verified</span>
                      ) : (
                        <span className="text-warning fw-semibold">Not verified</span>
                      )}
                    </div>
                  </div>
                  <div className="col mb-3">
                    <label className="mb-2 text-muted" htmlFor="Address">
                      Address
                    </label>
                    <input
                      id="Address"
                      type="text"
                      className="form-control"
                      name="Address"
                      required
                      value={address}
                      disabled={isDisabled}
                      onChange={(e) => setAddress(e.target.value)}
                    />
                  </div>
                </div>
                <div className="align-items-center d-flex flex-wrap gap-2 mb-4">
                  <button type="button" className="btn btn-primary" onClick={() => navigate('/user/')}>
                    Go Back
                  </button>
                  {isDisabled ? (
                    <button type="button" className="btn btn-success ms-auto" onClick={() => updateProfile()}>
                      Update Profile
                    </button>
                  ) : (
                    <button type="button" className="btn btn-success ms-auto" onClick={() => saveProfile()}>
                      Save Data
                    </button>
                  )}
                </div>

                <hr />
                <h2 className="h6 fw-bold text-muted mb-3">Verify phone number</h2>
                <p className="small text-muted mb-3">
                  We send a 6-digit code to your email on file. Changing your phone in the profile clears verification
                  until you confirm again.
                </p>
                <div className="d-flex flex-wrap gap-2 mb-3">
                  <button
                    type="button"
                    className="btn btn-outline-success btn-sm"
                    disabled={phoneBusy || !phoneNo}
                    onClick={sendPhoneCode}
                  >
                    Send code to email
                  </button>
                </div>
                <div className="row g-2 align-items-end">
                  <div className="col-sm-6">
                    <label className="form-label small text-muted mb-1" htmlFor="otp">
                      Code from email
                    </label>
                    <input
                      id="otp"
                      type="text"
                      inputMode="numeric"
                      autoComplete="one-time-code"
                      className="form-control"
                      placeholder="000000"
                      maxLength={6}
                      value={otpCode}
                      onChange={(e) => setOtpCode(e.target.value.replace(/\D/g, '').slice(0, 6))}
                    />
                  </div>
                  <div className="col-sm-auto">
                    <button
                      type="button"
                      className="btn btn-success"
                      disabled={phoneBusy || otpCode.length !== 6}
                      onClick={confirmPhoneCode}
                    >
                      Confirm code
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div
        className="text-center mt-8 text p-3 rounded"
        style={{ marginTop: '100px', marginBottom: '35px', backgroundColor: '#E0FFFF' }}
      >
        Copyright &copy; 2023 &mdash; FarmFresh
      </div>
    </div>
  );
}

export default UserProfile;

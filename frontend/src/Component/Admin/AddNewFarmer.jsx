import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import farmerServices from '../../Services/farmer.services';
import toast, { Toaster } from 'react-hot-toast';
import AdminNavBar from "./AdminNavBar";

function AddNewFarmer() {

    const [email, setEmail] = useState('');
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [phoneNo, setPhoneNo] = useState('');
    const [address, setAddress] = useState('');
    const { farmerId } = useParams();
    const navigate = useNavigate();

    const addFarmer = (e) => {
        e.preventDefault();
        const farmer = { email, firstname, lastname, phoneNo, address }
        console.log(farmer)

        if (farmerId) {
            // update
            farmerServices.updateFarmer(farmer, farmerId)
                .then(response => {
                    console.log('Farmer data updated successfully', response.data);
                    toast.success('Farmer Updated. Auto-Redirecting....',
                        {
                            style: {
                                borderRadius: '10px',
                                background: '#333',
                                color: '#fff',
                            },
                        }
                    )
                    setTimeout(() => {
                        navigate('/admin/farmer')
                    }, 2500)
                })
                .catch(error => {
                    console.log('Something went wrong', error);
                    toast.error('Something went wrong!',
                        {
                            style: {
                                borderRadius: '10px',
                                background: '#333',
                                color: '#fff',
                            },
                        }
                    )
                })
        } else {
            // create
            farmerServices.addFarmer(farmer)
                .then(respose => {
                    console.log("Farmer Registered.", respose.data)
                    toast.success('Farmer Added. Auto-Redirecting....',
                        {
                            style: {
                                borderRadius: '10px',
                                background: '#333',
                                color: '#fff',
                            },
                        }
                    )
                    setTimeout(() => {
                        navigate('/admin/farmer')
                    }, 2500)
                })
                .catch(error => {
                    console.log('Something Went Wrong', error);
                    toast.error('Something went wrong!',
                        {
                            style: {
                                borderRadius: '10px',
                                background: '#333',
                                color: '#fff',
                            },
                        }
                    )
                })
        }
    }


    useEffect(() => {
        if (farmerId) {
            farmerServices.getFarmerDetails(farmerId)
                .then(respose => {
                    console.log(respose.data);
                    setFirstname(respose.data.firstname);
                    setLastname(respose.data.lastname);
                    setPhoneNo(respose.data.phoneNo);
                    setAddress(respose.data.address)
                    setEmail(respose.data.email)
                })
                .catch(error => {
                    console.log('Something went wrong', error);
                })
        }
    }, [])

    return (
        <div style={{backgroundImage: 'url("https://render.fineartamerica.com/images/images-profile-flow/400/images/artworkimages/mediumlarge/1/farmers-market-patch-debbie-dewitt.jpg")',backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center center'}}>
            <AdminNavBar />
            <div className="container h-100">
                <div className="row justify-content-sm-center h-100">
                    <div className="col-xxl-6 col-xl-5 col-lg-5 col-md-7 col-sm-9">
                        <div className="text-center my-4">
                            <img src="https://static.wixstatic.com/media/0b5baa_ea7be66bc21b4d9b9ed2ad0df833d680~mv2.png/v1/fit/w_2500,h_1330,al_c/0b5baa_ea7be66bc21b4d9b9ed2ad0df833d680~mv2.png" alt="logo" width="100" style={{ borderRadius: '50px' }} />
                        </div>
                        <div className="card shadow-lg">
                            <div className="card-body px-5 pt-5">
                                <h1 className="fs-4 card-title fw-bold mb-4">{farmerId ? 'Update' : 'Add New'} Farmer</h1>
                                <form onSubmit={(e) => addFarmer(e)}>

                                    <div className="row g-3">
                                        <div className="col mb-3">
                                            <label className="mb-2 text-muted" htmlFor="firstname">First Name</label>
                                            <input id="firstname" type="text" className="form-control" name="firstname" required autoFocus
                                                value={firstname}
                                                onChange={(e) => setFirstname(e.target.value)}
                                            />

                                        </div>

                                        <div className="col mb-3">
                                            <label className="mb-2 text-muted" htmlFor="lastname">Last Name</label>
                                            <input id="lastname" type="text" className="form-control" name="lastname" required
                                                value={lastname}
                                                onChange={(e) => setLastname(e.target.value)}
                                            />
                                        </div>
                                    </div>

                                    <div className="row g-3">
                                        <div className="col mb-3">
                                            <label className="mb-2 text-muted" htmlFor="email">E-Mail Address</label>
                                            <input id="email" type="email" className="form-control" name="email" required
                                                pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
                                                value={email}
                                                onChange={(e) => setEmail(e.target.value)}
                                            />
                                        </div>

                                        <div className="col mb-3">
                                            <label className="mb-2 text-muted" htmlFor="phone">Phone No.</label>
                                            <input id="phone" type="tel" className="form-control" name="phone" required
                                                pattern="[0-9]{10}"
                                                value={phoneNo}
                                                onChange={(e) => setPhoneNo(e.target.value)}
                                            />
                                        </div>

                                    </div>

                                    <div className="row g-3">

                                        <div className="col mb-3">
                                            <label className="mb-2 text-muted" htmlFor="Address">Address</label>
                                            <textarea id="Address" className="form-control" name="Address" required rows="4" cols="50" maxLength="50"
                                                value={address}
                                                onChange={(e) => setAddress(e.target.value)}
                                            />
                                        </div>
                                    </div>

                                    <div className="align-items-center d-flex">
                                        <button type="submit" className="btn btn-primary">
                                            {farmerId ? 'Update' : 'Add New'} Farmer
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <div className="text-center mt-8 text p-3 rounded" style={{marginTop: '100px', marginBottom: '35px', backgroundColor: '#E0FFFF'}}>
                Copyright &copy; 2023 &mdash; FarmFresh
            </div>
        </div>
    );
}

export default AddNewFarmer;
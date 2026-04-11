import { useEffect, useState } from 'react';
import userServices from '../../Services/user.services';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import EmptyCart from './EmptyCart';
import UserNavBar from './UserNavBar';
import { useNavigate } from 'react-router-dom';
import { loadUser } from '../../auth/storage';
import { productImageUrlById } from '../../utils/imageUrls';

function CheckOut() {
  const [cart, setCart] = useState([]);
  const [totalAmount, setTotalAmout] = useState('');
  const [user, setUser] = useState(null);
  const [empty, setEmpty] = useState(false);
  const navigate = useNavigate();

  const init = () => {
    setUser(loadUser());
    userServices
      .checkOut()
      .then((response) => {
        setCart(response.data);
        let sum = 0;
        response.data.forEach((data) => {
          sum += parseFloat(data.amount);
        });
        setTotalAmout(sum);
        if (response.data.length === 0) {
          setEmpty(true);
        } else {
          setEmpty(false);
        }
      })
      .catch(() => {
        setEmpty(true);
      });
  };

  const removeItem = (lineId) => {
    userServices
      .removeItem(lineId)
      .then(() => init())
      .catch(() => {});
  };

  useEffect(() => {
    init();
  }, []);

  return (
    <div>
      <UserNavBar />
      <div className="container">
        <h3 className="mt-3 mb-3">Cart Details</h3>
        <hr />
        {empty ? (
          <EmptyCart />
        ) : (
          <div>
            <table
              className="table table-bordered table-striped text-center table-hover"
              style={{ verticalAlign: 'middle' }}
            >
              <thead className="thead-dark">
                <tr>
                  <th>Product Name</th>
                  <th>Quantity</th>
                  <th>Price per Unit</th>
                  <th>Amount</th>
                  <th className="text-center">Image</th>
                  <th className="text-center">Function</th>
                </tr>
              </thead>
              <tbody>
                {cart.map((c) => (
                  <tr key={c.lineId}>
                    <td>{c.item}</td>
                    <td>{c.qty}</td>
                    <td>{c.price}</td>
                    <td>{c.amount}</td>
                    <td className="text-center">
                      <img src={productImageUrlById(c.productId)} alt="product" width={75} />
                    </td>
                    <td>
                      <button type="button" className="btn btn-danger ml-2" onClick={() => removeItem(c.lineId)}>
                        Remove Item
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="d-grid gap-2 d-md-flex justify-content-md-end">
              <p className="fs-2">Total Amount: {totalAmount}</p>
            </div>
            <div className="d-grid gap-2 d-md-flex justify-content-md-end me-5">
              <button
                type="button"
                className="btn btn-primary btn-lg"
                onClick={() => navigate('/user/payment', { state: { totalAmount } })}
              >
                Place Order
              </button>
            </div>
          </div>
        )}
        <div>
          <Row md={3}>
            <Col>
              <Card style={{ backgroundColor: 'LightSteelBlue' }}>
                <Card.Body className="ms-5">
                  <Card.Title>
                    <p>Delivery Address: </p>
                  </Card.Title>
                  <Card.Text>
                    Name: {user ? `${user.firstname} ${user.lastname}` : ''}
                    <br />
                    Address: {user?.address}
                    <br />
                    Email: {user?.email}
                    <br />
                    Phone No.: {user?.phoneNo}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          </Row>
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

export default CheckOut;

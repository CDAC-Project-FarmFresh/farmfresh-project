import { useEffect, useState } from 'react';
import userServices from '../../Services/user.services';
import UserNavBar from './UserNavBar';
import { productImageUrlByName } from '../../utils/imageUrls';

function Orders() {
    const [order, setOrder] = useState([]);

    const init = () => {
        userServices
      .orders()
      .then((response) => {
        setOrder(response.data);
      })
      .catch(() => {});
  };

  useEffect(() => {
    init();
  }, []);

  return (
    <div style={{ backgroundColor: 'beige' }}>
      <UserNavBar />
      <div className="container">
        <h3 className="mt-3 mb-3">Order History</h3>
        <hr />
        <table
          className="table table-bordered table-striped text-center table-hover"
          style={{ verticalAlign: 'middle' }}
        >
          <thead className="thead-dark">
            <tr>
              <th>Product Name</th>
              <th>Quantity</th>
              <th>Amount</th>
              <th>Delivery Date</th>
              <th>Payment Status</th>
              <th>Order Place Date</th>
              <th className="text-center">Image</th>
            </tr>
          </thead>
          <tbody>
            {order.map((o) => (
              <tr key={o.id}>
                <td>{o.orderItem}</td>
                <td>{o.quantity}</td>
                <td>{o.amount}</td>
                <td>{o.orders?.deliveryDate}</td>
                <td>{o.orders?.paymentStatus ? 'Done' : 'Pending'}</td>
                <td>{o.orders?.placeOrderDate}</td>
                <td className="text-center">
                  <img src={productImageUrlByName(o.orderItem)} alt="product" width={75} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
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

export default Orders;

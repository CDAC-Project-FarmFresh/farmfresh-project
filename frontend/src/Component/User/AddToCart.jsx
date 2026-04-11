import { useState } from 'react';
import userServices from '../../Services/user.services';
import React from 'react';
import toast, { Toaster } from 'react-hot-toast';

function AddToCart(pid) {
  const [showCartBox, setShowCartBox] = useState(false);

  const addMe = (e) => {
    e.preventDefault();
    const qtyVal = e.target.buy_quantity.value;
    if (qtyVal === '') {
      setShowCartBox(false);
      return;
    }
    const qty = Number(qtyVal);
    userServices
      .addToCart(pid.productId, qty)
      .then(() => {
        toast.success('Product Added to Cart!', {
          position: 'bottom-right',
          style: {
            borderRadius: '10px',
            background: '#333',
            color: '#fff',
            height: '50px',
            fontSize: '15px',
          },
        });
      })
      .catch(() => {
        toast.error('Could not add to cart', {
          position: 'bottom-right',
        });
      });
    setShowCartBox(false);
  };

  return (
    <div>
      <Toaster position="bottom-right" />
      {showCartBox ? (
        <form onSubmit={addMe}>
          <div className="row">
            <div className="col-4">
              <input
                type="number"
                className="form-control form-control-sm"
                name="buy_quantity"
                style={{ width: '4rem' }}
                min="1"
              />
            </div>
            <div className="col">
              <button className="btn btn-primary btn-sm" type="submit">
                Add To Cart
              </button>
            </div>
          </div>
        </form>
      ) : (
        <button type="button" className="btn btn-primary" onClick={() => setShowCartBox(!showCartBox)}>
          Buy Product
        </button>
      )}
    </div>
  );
}

export default AddToCart;

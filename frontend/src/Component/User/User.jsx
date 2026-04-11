import { useState, useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import farmerServices from '../../Services/farmer.services';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import AddToCart from './AddToCart';
import UserNavBar from './UserNavBar';
import { productImageUrlById } from '../../utils/imageUrls';

function User() {
  const { data: products = [], isLoading } = useQuery({
    queryKey: ['products'],
    queryFn: async () => {
      const r = await farmerServices.getProductList();
      return r.data;
    },
  });

  const [sortKey, setSortKey] = useState('none');
  const [sortDir, setSortDir] = useState('asc');

  const sortedProducts = useMemo(() => {
    const list = [...products];
    const dir = sortDir === 'asc' ? 1 : -1;
    if (sortKey === 'name') {
      list.sort((a, b) => dir * a.stockItem.localeCompare(b.stockItem));
    } else if (sortKey === 'price') {
      list.sort((a, b) => dir * (a.pricePerUnit - b.pricePerUnit));
    } else if (sortKey === 'quantity') {
      list.sort((a, b) => dir * (a.quantity - b.quantity));
    }
    return list;
  }, [products, sortKey, sortDir]);

  return (
    <div
      style={{
        backgroundImage:
          'url("https://images.unsplash.com/photo-1500937386664-56d1df385ed9?w=1600")',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center center',
      }}
    >
      <UserNavBar />
      <div className="container mt-2">
        <div className="d-flex flex-wrap gap-2 justify-content-between align-items-center">
          <div className="dropdown btn-group">
            <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
              Sort descending
            </button>
            <ul className="dropdown-menu">
              <li>
                <button type="button" className="dropdown-item" onClick={() => { setSortKey('name'); setSortDir('desc'); }}>
                  Name
                </button>
              </li>
              <li>
                <button type="button" className="dropdown-item" onClick={() => { setSortKey('price'); setSortDir('desc'); }}>
                  Price
                </button>
              </li>
              <li>
                <button type="button" className="dropdown-item" onClick={() => { setSortKey('quantity'); setSortDir('desc'); }}>
                  Quantity
                </button>
              </li>
            </ul>
          </div>
          <div className="dropdown btn-group">
            <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
              Sort ascending
            </button>
            <ul className="dropdown-menu">
              <li>
                <button type="button" className="dropdown-item" onClick={() => { setSortKey('name'); setSortDir('asc'); }}>
                  Name
                </button>
              </li>
              <li>
                <button type="button" className="dropdown-item" onClick={() => { setSortKey('price'); setSortDir('asc'); }}>
                  Price
                </button>
              </li>
              <li>
                <button type="button" className="dropdown-item" onClick={() => { setSortKey('quantity'); setSortDir('asc'); }}>
                  Quantity
                </button>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div className="container mt-4 mb-5">
        {isLoading ? (
          <p>Loading products…</p>
        ) : (
          <Row xs={1} sm={2} md={4} className="g-4">
            {sortedProducts.map((p) => (
              <Col key={p.id}>
                <Card>
                  <Card.Img variant="top" src={productImageUrlById(p.id)} />
                  <Card.Body className="text-center">
                    <Card.Title>{p.stockItem}</Card.Title>
                    <Card.Text>
                      Amount: INR <span className="fw-bold">{p.pricePerUnit}</span>
                    </Card.Text>
                    <AddToCart productId={p.id} />
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        )}
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

export default User;

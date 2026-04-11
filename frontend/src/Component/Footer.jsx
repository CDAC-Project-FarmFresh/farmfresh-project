// Footer.js

import React from 'react';
import './Footer.css'; // Import CSS file for styling
import { Navbar, Nav, Carousel } from 'react-bootstrap';
import { useNavigate } from "react-router-dom";

function Footer() {
  return (
    <footer className="footer-container">
      <div className="footer-content">
        <div className="contact-info">
          <h3>Contact Us</h3>
          <p>Email: farmfresh@example.com</p>
          <p>Phone: +1234567890</p>
          {/* Add more contact information if needed */}
        </div>
        <div className="newsletter">
          <h3>Subscribe to our Newsletter</h3>
          <input type="email" placeholder="Your Email Address" />
          <button>Subscribe</button>
        </div>
        <div className="organization-info">
          <h3>Our Organization</h3>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget risus feugiat, congue odio nec, laoreet ex.</p>
          {/* Add more organization information if needed */}
        </div>
      </div>
      <div className="copyright">
        <p>&copy; 2022 Your Company. All rights reserved.</p>
      </div>
    </footer>
  );
}

export default Footer;

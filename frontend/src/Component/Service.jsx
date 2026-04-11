// Service.js

import React from 'react';
import { Navbar, Nav, Carousel } from 'react-bootstrap';
import './Service.css'; // Import CSS file for styling
import { useNavigate } from "react-router-dom";
import adminService from "../Services/admin.services";
import Footer from './Footer'; // Import Footer component


function Service() {
  return (
    <div className="service-container">
      <div className="service-content">
        <h2>Our Services</h2>
        <div className="service-item">
          <h3>Service 1</h3>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget risus feugiat, congue odio nec, laoreet ex.</p>
        </div>
        <div className="service-item">
          <h3>Service 2</h3>
          <p>Nullam nec leo risus. Vivamus vestibulum mi non libero euismod, vitae tincidunt ipsum tristique. Phasellus bibendum ipsum sit amet odio tempus, at tincidunt mi tempor.</p>
        </div>
        <div className="service-item">
          <h3>Service 3</h3>
          <p>Sed sit amet ligula non justo rutrum elementum id ac odio. Nullam scelerisque erat id quam condimentum, sed commodo arcu dapibus.</p>
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default Service;

// About.js
import React from 'react';
import './About.css'; // Import CSS file for styling
import { useNavigate } from "react-router-dom";
import adminService from "../Services/admin.services";
import image from '../images/carousel-img-one.jpg'
import Footer from './Footer'; // Import Footer component


function About() {
  return (
    <div className="about-container">
      <div className="about-content">
        <h2>About Us</h2>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget risus feugiat, congue odio nec, laoreet ex. Nullam nec leo risus. Vivamus vestibulum mi non libero euismod, vitae tincidunt ipsum tristique. Phasellus bibendum ipsum sit amet odio tempus, at tincidunt mi tempor. Sed sit amet ligula non justo rutrum elementum id ac odio. Nullam scelerisque erat id quam condimentum, sed commodo arcu dapibus. Nulla ut vestibulum velit.</p>
        <p>Nulla ut vestibulum velit. Sed eget risus feugiat, congue odio nec, laoreet ex. Nullam nec leo risus. Vivamus vestibulum mi non libero euismod, vitae tincidunt ipsum tristique. Phasellus bibendum ipsum sit amet odio tempus, at tincidunt mi tempor. Sed sit amet ligula non justo rutrum elementum id ac odio. Nullam scelerisque erat id quam condimentum, sed commodo arcu dapibus.</p>
        <img src= {image} alt="About Us" />
      </div>
      <Footer />
    </div>
  );
}

export default About;

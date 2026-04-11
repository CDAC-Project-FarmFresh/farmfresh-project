import { Navbar, Nav, Carousel } from 'react-bootstrap';
import { useNavigate } from "react-router-dom";
import adminService from "../Services/admin.services";
import slideOne from "../images/slide-one.jpg"
import slideTwo from "../images/slide-two.jpg"
import slideThree from "../images/grains.jpg"


function DefaultHome() {
return (
<div className="App">
        <Navbar bg="light" expand="lg">
          <Navbar.Brand href="/">
          <img src="https://assets1.progressivegrocer.com/files/2018-03/Farm%20Fresh%20Logo_0.png" alt="logo" width="100" style={{ borderRadius: '50px' }} /></Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
              <Nav.Link href="/">Home</Nav.Link>
              <Nav.Link href="/about">About</Nav.Link>
              <Nav.Link href="/services">Services</Nav.Link>
            </Nav>
            <Nav>
              <Nav.Link href="/signup">Sign In</Nav.Link>
              <Nav.Link href="/register">Register</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Navbar>

        <Carousel>
          <Carousel.Item>
            <img
              className="d-block w-100"
              src= {slideOne}
              alt="First slide"
            />
            <Carousel.Caption>
              <h3>First slide label</h3>
              <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
            </Carousel.Caption>
          </Carousel.Item>
          <Carousel.Item>
            <img
              className="d-block w-100"
              src= {slideTwo}
              alt="Second slide"
            />
            <Carousel.Caption>
              <h3>Second slide label</h3>
              <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
            </Carousel.Caption>
          </Carousel.Item>
          <Carousel.Item>
            <img
              className="d-block w-100"
              src= {slideThree}
              alt="Third slide"
            />
            <Carousel.Caption>
              <h3>Third slide label</h3>
              <p>Praesent commodo cursus magna, vel scelerisque nisl consectetur.</p>
            </Carousel.Caption>
          </Carousel.Item>
        </Carousel>
</div>
);
}

export default DefaultHome;
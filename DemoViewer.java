import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

//Model class for three coordinates (x,y,z)
//X -> movement left-right
//Y -> movement up-down
//Z -> away from/towards observer
class Vertex 
{
	 double x;
	 double y;
	 double z;
	 
	 Vertex(double x, double y, double z) 
	 {
	     this.x = x;
	     this.y = y;
	     this.z = z;
	 }
}

//Model class for triangle of coordinates and its color
class Triangle 
{
	Vertex v1;
	Vertex v2;
	Vertex v3;
	Color color;
 
	Triangle(Vertex v1, Vertex v2, Vertex v3, Color color)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
	}
}

//Model class for square of coordinates and its color
class Square
{
	Vertex v1;
	Vertex v2;
	Vertex v3;
	Vertex v4;
	Color color;

	Square(Vertex v1, Vertex v2, Vertex v3, Vertex v4, Color color)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
		this.color = color;
	}
}

// Model class for rotation (matrix-matrix & vector-matrix multiplication)
class Matrix3 
{
    double[] values;
    Matrix3(double[] values) 
    {
        this.values = values;
    }
    
    Matrix3 multiply(Matrix3 other) 
    {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row * 3 + col] +=
                        this.values[row * 3 + i] * other.values[i * 3 + col];
                }
            }
        }
        return new Matrix3(result);
    }
    
    Vertex transform(Vertex in) 
    {
        return new Vertex(
            in.x * values[0] + in.y * values[3] + in.z * values[6],
            in.x * values[1] + in.y * values[4] + in.z * values[7],
            in.x * values[2] + in.y * values[5] + in.z * values[8]
        );
    }
}

public class DemoViewer 
{
	
	// main function
    public static void main(String[] args) 
    {
    	// Create JFrame and set borders
        JFrame frame = new JFrame();
        frame.setTitle("Simple Java 3D Shape Visualizer");
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        
        // slider to control horizontal rotation
        JSlider headingSlider = new JSlider(0, 360, 180);
        pane.add(headingSlider, BorderLayout.SOUTH);

        // slider to control vertical rotation
        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(pitchSlider, BorderLayout.EAST);
        
        // Create buttons and subpanel
        JPanel subPane = new JPanel();
        JButton buttonS = new JButton("Sphere");
        JButton buttonT = new JButton("Tetrahedron");
        JButton buttonC = new JButton("Cube");
        JButton buttonP = new JButton("Pyramid");
        JButton buttonPr = new JButton("Prism");
        JButton buttonO = new JButton("Octahedron");
        JButton buttonQ = new JButton("Quit");
        
        // Add button to JPanel
        subPane.add(buttonT);
        subPane.add(buttonS);
        subPane.add(buttonC);
        subPane.add(buttonP);
        subPane.add(buttonPr);
        subPane.add(buttonO);
        subPane.add(buttonQ);
        pane.add(subPane, BorderLayout.NORTH);

        // Flag for inflation
        AtomicInteger sflag = new AtomicInteger();
        
        // panel to display render results
		JPanel renderPanel = new JPanel() 
        {
			private static final long serialVersionUID = 1L;
			
				public void paintComponent(Graphics g) 
                {
					
					// Color background
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.BLACK);
					g2.fillRect(0, 0, getWidth(), getHeight());

					// Create tetrahedron from 4 triangles
					ArrayList<Triangle> tris = new ArrayList<>();
					tris.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(-100, -100, 100),
							new Vertex(-100, 100, -100),
							Color.WHITE));
					tris.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(-100, -100, 100),
							new Vertex(100, -100, -100),
							Color.RED));
					tris.add(new Triangle(new Vertex(-100, 100, -100),
							new Vertex(100, -100, -100),
							new Vertex(100, 100, 100),
							Color.GREEN));
					tris.add(new Triangle(new Vertex(-100, 100, -100),
							new Vertex(100, -100, -100),
							new Vertex(-100, -100, 100),
							Color.BLUE));

					// Create cube from 12 triangles
					ArrayList<Triangle> cube = new ArrayList<>();
					cube.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(-100, -100, -100),
							new Vertex(-100, 100, -100),
							Color.RED));
					cube.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(-100, -100, -100),
							new Vertex(100, -100, 100),
							Color.RED));
					cube.add(new Triangle(new Vertex(100, -100, -300),
							new Vertex(-100, -100, -100),
							new Vertex(-100, 100, -100),
							Color.GREEN));
					cube.add(new Triangle(new Vertex(100, -100, -300),
							new Vertex(-100, 100, -100),
							new Vertex(100, 100, -300), 
							Color.GREEN));
					cube.add(new Triangle(new Vertex(300, 100, -100),
							new Vertex(100, 100, 100),
							new Vertex(100, -100, 100),
							Color.YELLOW));
					cube.add(new Triangle(new Vertex(300, 100, -100),
							new Vertex(100, -100, 100),
							new Vertex(300, -100, -100),
							Color.YELLOW));
					cube.add(new Triangle(new Vertex(300, 100, -100),
							new Vertex(100, 100, -300),
							new Vertex(100, -100, -300),
							Color.WHITE));
					cube.add(new Triangle(new Vertex(300, -100, -100),
							new Vertex(300, 100, -100),
							new Vertex(100, -100, -300),
							Color.WHITE));
					cube.add(new Triangle(new Vertex(100, -100, -300),
							new Vertex(-100, -100, -100),
							new Vertex(300, -100, -100),
							Color.ORANGE));
					cube.add(new Triangle(new Vertex(100, -100, 100),
							new Vertex(-100, -100, -100),
							new Vertex(300, -100, -100),
							Color.ORANGE));
					cube.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(300, 100, -100),
							new Vertex(-100, 100, -100),
							Color.BLUE));
					cube.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(300, 100, -100),
							new Vertex(-100, 100, -100),
							Color.BLUE));

					// Create pyramid from 6 triangles
					ArrayList<Triangle> pyra = new ArrayList<>();
					pyra.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(300, 100, -100),
							new Vertex(-100, 100, -100),
							Color.WHITE));
					pyra.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(300, 100, -100),
							new Vertex(-100, 100, -100),
							Color.WHITE));
					pyra.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(300, 100, -100),
							new Vertex(100, -200, -100), // Peak of pyramid
							Color.YELLOW));
					pyra.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(-100, 100, -100),
							new Vertex(100, -200, -100), // Peak of pyramid
							Color.ORANGE));
					pyra.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(300, 100, -100),
							new Vertex(100, -200, -100), // Peak of pyramid
							Color.YELLOW));
					pyra.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(-100, 100, -100),
							new Vertex(100, -200, -100), // Peak of pyramid
							Color.ORANGE));
					
					// Create prism from 8 triangles
					ArrayList<Triangle> prism = new ArrayList<>();
					prism.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(300, 100, -100),
							new Vertex(-100, 100, -100),
							Color.WHITE));
					prism.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(300, 100, -100),
							new Vertex(-100, 100, -100),
							Color.WHITE));
					prism.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(300, 100, -100),
							new Vertex(200, -100, -200), // Peak #1
							Color.RED));
					prism.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(-100, 100, -100),
							new Vertex(0, -100, 0), // Peak #2
							Color.RED));
					prism.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(300, 100, -100),
							new Vertex(0, -100, 0), // Peak #2
							Color.RED));
					prism.add(new Triangle(new Vertex(200, -100, -200), // Peak #1
							new Vertex(0, -100, 0), // Peak #2
							new Vertex(-100, 100, -100),
							Color.RED));
					prism.add(new Triangle(new Vertex(200, -100, -200), // Peak #1
							new Vertex(0, -100, 0), // Peak #2
							new Vertex(300, 100, -100),
							Color.RED));
					prism.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(200, -100, -200), // Peak #1
							new Vertex(-100, 100, -100),
							Color.RED));
					
					// Create octahedron from two pyramids
					ArrayList<Triangle> octa = new ArrayList<>();
					octa.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(300, 100, -100),
							new Vertex(100, -150, -100), // Peak of pyramid 1
							Color.YELLOW));
					octa.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(-100, 100, -100),
							new Vertex(100, -150, -100), // Peak of pyramid 1
							Color.ORANGE));
					octa.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(300, 100, -100),
							new Vertex(100, -150, -100), // Peak of pyramid 1
							Color.YELLOW));
					octa.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(-100, 100, -100),
							new Vertex(100, -150, -100), // Peak of pyramid 1
							Color.ORANGE));
					octa.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(300, 100, -100),
							new Vertex(100, 300, -100), // Peak of pyramid 2
							Color.YELLOW));
					octa.add(new Triangle(new Vertex(100, 100, -300),
							new Vertex(-100, 100, -100),
							new Vertex(100, 300, -100), // Peak of pyramid 2
							Color.ORANGE));
					octa.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(300, 100, -100),
							new Vertex(100, 300, -100), // Peak of pyramid 2
							Color.YELLOW));
					octa.add(new Triangle(new Vertex(100, 100, 100),
							new Vertex(-100, 100, -100),
							new Vertex(100, 300, -100), // Peak of pyramid 2
							Color.ORANGE));
					
                    switch(sflag.intValue())
                    {
                    	case 1:
                    		for (int i = 0; i < 6; i++) 
                    		{
                    			tris = inflate(tris);
                    		}
                    		break;
                    	case 0:
                    	default:
                    }
                    
                    
                    // Rotation matrix for sliders
                    // Pitch for Up to Down
                    // Heading for Left to Right
                    double heading = Math.toRadians(headingSlider.getValue());
                    
                    Matrix3 headingTransform = new Matrix3(new double[] {
                            Math.cos(heading), 0, Math.sin(heading),
                            0, 1, 0,
                            -Math.sin(heading), 0, Math.cos(heading)
                        });
                    double pitch = Math.toRadians(pitchSlider.getValue());
                    Matrix3 pitchTransform = new Matrix3(new double[] {
                            1, 0, 0,
                            0, Math.cos(pitch), Math.sin(pitch),
                            0, -Math.sin(pitch), Math.cos(pitch)
                        });
                    Matrix3 transform = headingTransform.multiply(pitchTransform);
                    
                    // Draw tetrahedron
                    BufferedImage img = 
                    	    new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                    
                    double[] zBuffer = new double[img.getWidth() * img.getHeight()];
                    // initialize array with extremely far away depths
                    for (int q = 0; q < zBuffer.length; q++) 
                    {
                        zBuffer[q] = Double.NEGATIVE_INFINITY;
                    }
                    
                    // Case of tetrahedron and sphere
                    if (sflag.intValue() == 0 || sflag.intValue() == 1) 
                    {
	                    for (Triangle t : tris) 
	                    {
	                    	   Vertex v1 = transform.transform(t.v1);
	                    	   Vertex v2 = transform.transform(t.v2);
	                    	   Vertex v3 = transform.transform(t.v3);
	
	                    	    // we have to do translation manually
	                    	    v1.x += getWidth() / 2;
	                    	    v1.y += getHeight() / 2;
	                    	    v2.x += getWidth() / 2;
	                    	    v2.y += getHeight() / 2;
	                    	    v3.x += getWidth() / 2;
	                    	    v3.y += getHeight() / 2;
	
	                    	    // compute rectangular bounds for triangle
	                    	    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
	                    	    int maxX = (int) Math.min(img.getWidth() - 1, 
	                    	                              Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
	                    	    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
	                    	    int maxY = (int) Math.min(img.getHeight() - 1,
	                    	                              Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
	
	                    	    double triangleArea =
	                    	       (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
	                    	    
	                    	    // transform vertices before calculating normal...
	                    	    Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
	                            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
	                    	    Vertex norm = new Vertex(
	                    	         ab.y * ac.z - ab.z * ac.y,
	                    	         ab.z * ac.x - ab.x * ac.z,
	                    	         ab.x * ac.y - ab.y * ac.x
	                    	    );
	                    	    
	                    	    double normalLength =
	                    	        Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
	                    	    norm.x /= normalLength;
	                    	    norm.y /= normalLength;
	                    	    norm.z /= normalLength;
	                    	    
	                    	    double angleCos = Math.abs(norm.z);
	                    	    
	
	                    	    for (int y = minY; y <= maxY; y++) 
	                    	    {
	                    	        for (int x = minX; x <= maxX; x++) 
	                    	        {
	                    	            double b1 = 
	                    	              ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
	                    	            double b2 =
	                    	              ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
	                    	            double b3 =
	                    	              ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
	                    	            
	                    	            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) 
	                    	            {
	                    	            	 double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
	                                         int zIndex = y * img.getWidth() + x;
	                                         if (zBuffer[zIndex] < depth) 
	                                         {
	                                             img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
	                                             zBuffer[zIndex] = depth;
	                                         }
	                    	            }
	                    	        }
	                    	    }
                    		}   
                    } 
                    // Case of cube
                    else if(sflag.intValue() == 2)
                    {
                    	for (Triangle t : cube) 
	                    {
	                    	   Vertex v1 = transform.transform(t.v1);
	                    	   Vertex v2 = transform.transform(t.v2);
	                    	   Vertex v3 = transform.transform(t.v3);
	
	                    	    // we have to do translation manually
	                    	    v1.x += getWidth() / 2;
	                    	    v1.y += getHeight() / 2;
	                    	    v2.x += getWidth() / 2;
	                    	    v2.y += getHeight() / 2;
	                    	    v3.x += getWidth() / 2;
	                    	    v3.y += getHeight() / 2;

	
	                    	    // compute rectangular bounds for triangle
	                    	    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
	                    	    int maxX = (int) Math.min(img.getWidth() - 1, 
	                    	                              Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
	                    	    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
	                    	    int maxY = (int) Math.min(img.getHeight() - 1,
	                    	                              Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
	
	                    	    double triangleArea =
	                    	       (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
	                    	    
	                    	    // transform vertices before calculating normal...
	                    	    Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
	                            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
	                    	    Vertex norm = new Vertex(
	                    	         ab.y * ac.z - ab.z * ac.y,
	                    	         ab.z * ac.x - ab.x * ac.z,
	                    	         ab.x * ac.y - ab.y * ac.x
	                    	    );
	                    	    
	                    	    double normalLength =
	                    	        Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
	                    	    norm.x /= normalLength;
	                    	    norm.y /= normalLength;
	                    	    norm.z /= normalLength;
	                    	    
	                    	    double angleCos = Math.abs(norm.z);
	                    	    
	
	                    	    for (int y = minY; y <= maxY; y++) 
	                    	    {
	                    	        for (int x = minX; x <= maxX; x++) 
	                    	        {
	                    	            double b1 = 
	                    	              ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
	                    	            double b2 =
	                    	              ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
	                    	            double b3 =
	                    	              ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
	                    	            
	                    	            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) 
	                    	            {
	                    	            	 double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
	                                         int zIndex = y * img.getWidth() + x;
	                                         if (zBuffer[zIndex] < depth) 
	                                         {
	                                             img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
	                                             zBuffer[zIndex] = depth;
	                                         }
	                    	            }
	                    	        }
	                    	    }
                    		}   
                    }
                    
                    else if(sflag.intValue() == 3)
                    {
                    	for (Triangle t : pyra) 
	                    {
	                    	   Vertex v1 = transform.transform(t.v1);
	                    	   Vertex v2 = transform.transform(t.v2);
	                    	   Vertex v3 = transform.transform(t.v3);
	
	                    	    // we have to do translation manually
	                    	    v1.x += getWidth() / 2;
	                    	    v1.y += getHeight() / 2;
	                    	    v2.x += getWidth() / 2;
	                    	    v2.y += getHeight() / 2;
	                    	    v3.x += getWidth() / 2;
	                    	    v3.y += getHeight() / 2;

	
	                    	    // compute rectangular bounds for triangle
	                    	    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
	                    	    int maxX = (int) Math.min(img.getWidth() - 1, 
	                    	                              Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
	                    	    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
	                    	    int maxY = (int) Math.min(img.getHeight() - 1,
	                    	                              Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
	
	                    	    double triangleArea =
	                    	       (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
	                    	    
	                    	    // transform vertices before calculating normal...
	                    	    Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
	                            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
	                    	    Vertex norm = new Vertex(
	                    	         ab.y * ac.z - ab.z * ac.y,
	                    	         ab.z * ac.x - ab.x * ac.z,
	                    	         ab.x * ac.y - ab.y * ac.x
	                    	    );
	                    	    
	                    	    double normalLength =
	                    	        Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
	                    	    norm.x /= normalLength;
	                    	    norm.y /= normalLength;
	                    	    norm.z /= normalLength;
	                    	    
	                    	    double angleCos = Math.abs(norm.z);
	                    	    
	
	                    	    for (int y = minY; y <= maxY; y++) 
	                    	    {
	                    	        for (int x = minX; x <= maxX; x++) 
	                    	        {
	                    	            double b1 = 
	                    	              ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
	                    	            double b2 =
	                    	              ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
	                    	            double b3 =
	                    	              ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
	                    	            
	                    	            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) 
	                    	            {
	                    	            	 double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
	                                         int zIndex = y * img.getWidth() + x;
	                                         if (zBuffer[zIndex] < depth) 
	                                         {
	                                             img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
	                                             zBuffer[zIndex] = depth;
	                                         }
	                    	            }
	                    	        }
	                    	    }
                    		}   
                    }
                    
                    else if(sflag.intValue() == 4)
                    {
                    	for (Triangle t : prism) 
	                    {
	                    	   Vertex v1 = transform.transform(t.v1);
	                    	   Vertex v2 = transform.transform(t.v2);
	                    	   Vertex v3 = transform.transform(t.v3);
	
	                    	    // we have to do translation manually
	                    	    v1.x += getWidth() / 2;
	                    	    v1.y += getHeight() / 2;
	                    	    v2.x += getWidth() / 2;
	                    	    v2.y += getHeight() / 2;
	                    	    v3.x += getWidth() / 2;
	                    	    v3.y += getHeight() / 2;

	
	                    	    // compute rectangular bounds for triangle
	                    	    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
	                    	    int maxX = (int) Math.min(img.getWidth() - 1, 
	                    	                              Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
	                    	    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
	                    	    int maxY = (int) Math.min(img.getHeight() - 1,
	                    	                              Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
	
	                    	    double triangleArea =
	                    	       (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
	                    	    
	                    	    // transform vertices before calculating normal...
	                    	    Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
	                            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
	                    	    Vertex norm = new Vertex(
	                    	         ab.y * ac.z - ab.z * ac.y,
	                    	         ab.z * ac.x - ab.x * ac.z,
	                    	         ab.x * ac.y - ab.y * ac.x
	                    	    );
	                    	    
	                    	    double normalLength =
	                    	        Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
	                    	    norm.x /= normalLength;
	                    	    norm.y /= normalLength;
	                    	    norm.z /= normalLength;
	                    	    
	                    	    double angleCos = Math.abs(norm.z);
	                    	    
	
	                    	    for (int y = minY; y <= maxY; y++) 
	                    	    {
	                    	        for (int x = minX; x <= maxX; x++) 
	                    	        {
	                    	            double b1 = 
	                    	              ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
	                    	            double b2 =
	                    	              ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
	                    	            double b3 =
	                    	              ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
	                    	            
	                    	            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) 
	                    	            {
	                    	            	 double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
	                                         int zIndex = y * img.getWidth() + x;
	                                         if (zBuffer[zIndex] < depth) 
	                                         {
	                                             img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
	                                             zBuffer[zIndex] = depth;
	                                         }
	                    	            }
	                    	        }
	                    	    }
                    		}   
                    }
                    
                    else if(sflag.intValue() == 5)
                    {
                    	for (Triangle t : octa) 
	                    {
	                    	   Vertex v1 = transform.transform(t.v1);
	                    	   Vertex v2 = transform.transform(t.v2);
	                    	   Vertex v3 = transform.transform(t.v3);
	
	                    	    // we have to do translation manually
	                    	    v1.x += getWidth() / 2;
	                    	    v1.y += getHeight() / 2;
	                    	    v2.x += getWidth() / 2;
	                    	    v2.y += getHeight() / 2;
	                    	    v3.x += getWidth() / 2;
	                    	    v3.y += getHeight() / 2;

	
	                    	    // compute rectangular bounds for triangle
	                    	    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
	                    	    int maxX = (int) Math.min(img.getWidth() - 1, 
	                    	                              Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
	                    	    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
	                    	    int maxY = (int) Math.min(img.getHeight() - 1,
	                    	                              Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
	
	                    	    double triangleArea =
	                    	       (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
	                    	    
	                    	    // transform vertices before calculating normal...
	                    	    Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
	                            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
	                    	    Vertex norm = new Vertex(
	                    	         ab.y * ac.z - ab.z * ac.y,
	                    	         ab.z * ac.x - ab.x * ac.z,
	                    	         ab.x * ac.y - ab.y * ac.x
	                    	    );
	                    	    
	                    	    double normalLength =
	                    	        Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
	                    	    norm.x /= normalLength;
	                    	    norm.y /= normalLength;
	                    	    norm.z /= normalLength;
	                    	    
	                    	    double angleCos = Math.abs(norm.z);
	                    	    
	
	                    	    for (int y = minY; y <= maxY; y++) 
	                    	    {
	                    	        for (int x = minX; x <= maxX; x++) 
	                    	        {
	                    	            double b1 = 
	                    	              ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
	                    	            double b2 =
	                    	              ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
	                    	            double b3 =
	                    	              ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
	                    	            
	                    	            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) 
	                    	            {
	                    	            	 double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
	                                         int zIndex = y * img.getWidth() + x;
	                                         if (zBuffer[zIndex] < depth) 
	                                         {
	                                             img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
	                                             zBuffer[zIndex] = depth;
	                                         }
	                    	            }
	                    	        }
	                    	    }
                    		}   
                    }
                    
                    g2.drawImage(img, 0, 0, null);
                }
            };
            
        
        // add a listeners on button to force redraw
        buttonS.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e)
        	{  
        		sflag.lazySet(1);
        		renderPanel.repaint();
            } 
        });
        buttonT.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e)
        	{  
        		sflag.lazySet(0);
        		renderPanel.repaint();
            } 
        });
        buttonC.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e)
        	{  
        		sflag.lazySet(2);
        		renderPanel.repaint();
            } 
        });
        buttonP.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e)
        	{  
        		sflag.lazySet(3);
        		renderPanel.repaint();
            } 
        });
        buttonPr.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e)
        	{  
        		sflag.lazySet(4);
        		renderPanel.repaint();
            } 
        });
        buttonO.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e)
        	{  
        		sflag.lazySet(5);
        		renderPanel.repaint();
            } 
        });
        buttonQ.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e)
        	{  
        		System.exit(0);
            } 
        });
        
        // add a listeners on heading and pitch sliders to force redraw
        headingSlider.addChangeListener(e -> renderPanel.repaint());
        pitchSlider.addChangeListener(e -> renderPanel.repaint());

        // Display renderPanel
        pane.add(renderPanel, BorderLayout.CENTER);

        frame.setSize(750, 750);
        frame.setVisible(true);
        
    }
    
    // Function to shade in triangles
    public static Color getShade(Color color, double shade) 
    {
    	double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1/2.4);
        int green = (int) Math.pow(greenLinear, 1/2.4);
        int blue = (int) Math.pow(blueLinear, 1/2.4);

        return new Color(red, green, blue);
    }
    
    // Turn tetrahedron into sphere by inflating triangles
    public static ArrayList<Triangle> inflate(ArrayList<Triangle> tris) 
    {
    	ArrayList<Triangle> result = new ArrayList<>();
        for (Triangle t : tris) 
        {
            Vertex m1 = new Vertex((t.v1.x + t.v2.x)/2, (t.v1.y + t.v2.y)/2, (t.v1.z + t.v2.z)/2);
            Vertex m2 = new Vertex((t.v2.x + t.v3.x)/2, (t.v2.y + t.v3.y)/2, (t.v2.z + t.v3.z)/2);
            Vertex m3 = new Vertex((t.v1.x + t.v3.x)/2, (t.v1.y + t.v3.y)/2, (t.v1.z + t.v3.z)/2);
            result.add(new Triangle(t.v1, m1, m3, t.color));
            result.add(new Triangle(t.v2, m1, m2, t.color));
            result.add(new Triangle(t.v3, m2, m3, t.color));
            result.add(new Triangle(m1, m2, m3, t.color));
        }
        for (Triangle t : result) {
            for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 }) {
                double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / Math.sqrt(30000);
                v.x /= l;
                v.y /= l;
                v.z /= l;
            }
        }
        return result;
    }
}



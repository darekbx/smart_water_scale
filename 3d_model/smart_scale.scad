$fn = 45;

plate_thickness = 3.5;
hole_diameter = 2.9;
leg_diameter = 10.5;

sensor_height = 34;
sensor_width = 44 - 1.5; // 1.5 is a mount bolt


top();

//bottom();

/**
 * Top plate
 */
module top() {
    plate_size = 115;
    mount_d = 3.1;
    mount_s = 6.1;
    mount_h = 3;
   
    difference() {
        cube([plate_size, plate_size, 2]);

        // side cut-outs
        translate([-0.1, (plate_size - 30) / 2, -1]) cube([10, 30, 4]);
        translate([plate_size - 10 + 0.1, (plate_size - 30) / 2, -1]) cube([10, 30, 4]);

        translate([(plate_size - 70) / 2, -0.1, -1]) cube([70, 10, 4]);
        translate([(plate_size - 70) / 2, plate_size - 10 + 0.1, -1]) cube([70, 10, 4]);
        
        // center cut-out
        translate([plate_size / 2, plate_size / 2, -1]) cylinder(d = 40, h = 4);
          
        // holes
        translate([0.5, 1, 0]) {
            // mount h 75mm
            mount_h_space = 76;
            translate([7, (plate_size - mount_h_space) / 2 - 1, -0.5]) {
                cylinder(d = mount_d, h = mount_h);
                translate([0, 0, -1]) cylinder(d = mount_s, h = mount_h);
            }
            translate([plate_size - 7, (plate_size - mount_h_space) / 2 - 1, -0.5]) { 
                cylinder(d = mount_d, h = mount_h); 
                translate([0, 0, -1]) cylinder(d = mount_s, h = mount_h);
            }
            
            // mount w 100mm
            mount_w_space = 100;
            translate([plate_size - 7, plate_size - 19.5, -0.5]) {
                cylinder(d = mount_d, h = mount_h);
                translate([0, 0, -1]) cylinder(d = mount_s, h = mount_h);
            }
            translate([(plate_size - mount_w_space) / 2 - 1, plate_size - 19.5, -0.5]) {
                cylinder(d = mount_d, h = mount_h);
                translate([0, 0, -1]) cylinder(d = mount_s, h = mount_h);
            }
        }
    }
    
    // supports
    difference() {
        support_h = 4;
        color([1,0,0]) translate([0, 0, 0]) {
            support_size_w = 70;
            translate([(plate_size - support_size_w) / 2, 22.5, 1.8]) cube([support_size_w, 2, support_h]);
            translate([(plate_size - support_size_w) / 2, plate_size - 2 - 22.5, 1.8]) cube([support_size_w, 2, support_h]);
            
            support_size_h = 70;
            translate([22, (plate_size - support_size_h) / 2, 1.8]) cube([2, support_size_h, support_h]);
            translate([plate_size - 24, (plate_size - support_size_h) / 2, 1.8]) cube([2, support_size_h, support_h]);
        }
        
        translate([16, 8, 7]) rotate([0, 45, 45]) cube(14);
        translate([plate_size - 20, plate_size - 32, 7]) rotate([0, 45, 45]) cube(14);
        translate([16, plate_size - 26, 0]) rotate([45, 0, 45]) cube(14);
        translate([plate_size - 26, 16, 0]) rotate([45, 0, 45]) cube(14);
    }
}

/**
 * Bottom plate
 */
module bottom() {
    difference() { 
        translate([0, 0, 0]) {
            bottom_plate();
            translate([20, 52, 0]) rotate([0, 0, 90]) unit_mount();
        }
        // cut from bottom
        translate([-5, -5, -4.4]) cube([130, 130, 5]);
    }
}

module unit_mount() {
    mount_diameter = 4.8;
    translate([0.05, 0, 0]) cylinder(d = mount_diameter, h = 10);
    translate([5 + 10.95, 0, 0]) cylinder(d = mount_diameter, h = 10);
    translate([-4, -3, 0]) cube([24, 23, plate_thickness]);
}

module bottom_plate() {
    plate_right();
    translate([76, 0, 0]) plate_left();
    translate([120, 120, 0]) rotate([0, 0, 180]) plate_right();
    translate([44, 120, 0]) rotate([0, 0, 180]) plate_left();
    
    translate([40, 0, 0]) side_wall_h();
    translate([40, 106, 0]) side_wall_h();
    translate([0, 30, 0]) side_wall_w();
    translate([106, 30, 0]) side_wall_w();
}

module side_wall_h() {
    difference() {
        cube([40, 14, plate_thickness]);
        
        translate([6, 3.5, -1])  
            minkowski() {
                cube([28, 7, 5]);
                cylinder(d = 2, h = 0.1);
            }
    }
}

module side_wall_w() {
    difference() {
        cube([14, 60, plate_thickness]);
        translate([3.5, 7.5, -1]) 
            minkowski() {
                cube([7, 45, 5]);
                cylinder(d = 2, h = 0.1);
            }
    }
}

module plate_right() {
    difference() {
        cube([44, 35, plate_thickness]);
        translate([0, 0, 3.51]) sensor_right();
        translate([16, 16, -1]) cylinder(d = leg_diameter, h = 2.25);
        
        translate([5, 5, 2.5]) 
            minkowski() { 
                cube([33, 24, 2]);
                cylinder(d = 2, h = 0.1);
            }
    }
    translate([0, 0, 2]) {
        sensor_bolts(7);
    }
}

module plate_left() {
    difference() {
        cube([44, 35, plate_thickness]);
        translate([1.5, 0, 3.51]) sensor_left();
        translate([30, 16, -1]) cylinder(d = leg_diameter, h = 2.25);
        
        translate([6, 5, 2.5]) 
            minkowski() { 
                cube([33, 24, 2]);
                cylinder(d = 2, h = 0.1);
            }
    }
    translate([1.5, 0, 2]) {
        sensor_bolts(7);
    }
}

module sensor_right() {
    height = 34;
    width = 44 - 1.5; // 1.5 is a mount bolt
    translate([-1, height - 5 - 5.5, -3]) cube([3, 5, 3]);
    translate([5, -1, -3]) cube([5, 3, 3]);
    translate([width - 2.5 - 7, -1, -3]) cube([5, 3, 3]);
}

module sensor_left() {
    height = 34;
    width = 44 - 1.5; // 1.5 is a mount bolt
    translate([width - 2.5, height - 5 - 5.5, -3]) cube([3, 5, 3]);
    translate([5, -1, -3]) cube([5, 3, 3]);
    translate([width - 2.5 - 7, -1, -3]) cube([5, 3, 3]);
}

module sensor_bolts(bolt_height) {
    offset_w = 1.4;
    offset_h = 1.9;
    translate([offset_w + 1, offset_h + 1, -1]) cylinder(d = hole_diameter, h = bolt_height);
    translate([sensor_width - offset_w - 1, offset_h + 1, -1]) cylinder(d = hole_diameter, h = bolt_height);
    translate([sensor_width - offset_w - 1, sensor_height - 1.8 - 1, -1]) cylinder(d = hole_diameter, h = bolt_height);
    translate([offset_w + 1, sensor_height - offset_h - 1, -1]) cylinder(d = hole_diameter, h = bolt_height);
}
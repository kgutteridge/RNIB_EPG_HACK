//
//  TVGuideViewController.m
//  SkyEPG
//
//  Created by Kieran Gutteridge on 12/02/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "TVGuideViewController.h"

@interface TVGuideViewController()

-(void)releaseOutlets;

@end

@implementation TVGuideViewController

@synthesize programDetail;
@synthesize recordButton;
@synthesize textView;


-(void)releaseOutlets
{
    self.recordButton = nil;
    self.textView = nil;
}

-(void)dealloc
{
    self.programDetail = nil;
    [self releaseOutlets];
    
    [super dealloc];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    NSString *programDesc =  [self.programDetail objectForKey:@"shortDesc"];
    textView.text = programDesc;
    
    NSString *programName = [self.programDetail objectForKey:@"title"];
    [recordButton setTitle:[NSString stringWithFormat:@"%@",programName] forState:UIControlStateNormal];
    [recordButton setAccessibilityLabel:[NSString stringWithFormat:@"%@",programName]];
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


#pragma mark -
#pragma mark UIActions

-(IBAction)recordButtonAction:(id)sender
{
    
}


@end

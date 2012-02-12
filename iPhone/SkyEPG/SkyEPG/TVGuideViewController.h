//
//  TVGuideViewController.h
//  SkyEPG
//
//  Created by Kieran Gutteridge on 12/02/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TVGuideViewController : UIViewController

@property (nonatomic, retain) NSDictionary *programDetail;
@property (nonatomic, retain) IBOutlet UIButton *recordButton;
@property (nonatomic, retain) IBOutlet UITextView *textView;


-(IBAction)recordButtonAction:(id)sender;

@end

//
//  FirstViewController.m
//  SkyEPG
//
//  Created by Kieran Gutteridge on 11/02/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "FirstViewController.h"

#import "AFNetworking.h"

#import "JSONKit.h"

#import "TVGuideViewController.h"

@interface FirstViewController ()

-(void)releaseOutlets;

@end

@implementation FirstViewController

@synthesize channelIDs, channelDetails;
@synthesize tableView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = NSLocalizedString(@"Whats on now", @"Whats on now");
        self.tabBarItem.image = [UIImage imageNamed:@"first"];
    }
    return self;
}
							
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

-(void)releaseOutlets
{
    self.tableView = nil;
}

-(void)dealloc
{
    self.channelIDs = nil;
    
    [self releaseOutlets];
    
    [super dealloc];
}


#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
  
    
    NSURL *url = [NSURL URLWithString:@"http://epgservices.sky.com/tvlistings-proxy/TVListingsProxy/init.json"];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];

    AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request 

                                        success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) 
                                        {
                                            //NSLog(@"Channels: %@", [JSON valueForKeyPath:@"channels"]);
                                            
                                            NSArray *dictionary = [JSON objectForKey:@"channels"];
            
                                            NSMutableArray *ids = [NSMutableArray array];
                                            
                                            for (NSDictionary *detail in dictionary) 
                                            {
                                                [ids addObject:[detail objectForKey:@"channelid"]];
                                            }
                                            
                                            self.channelIDs = ids;
                                            
                                            [self parseChannelInfo];
                                            
                                        } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON) 
                                         {
                                             NSLog(@"Problem getting Sky init %@",error);
                                         }];
    [operation start];
}


-(void)parseChannelInfo
{
    
    
    NSMutableString *channelIDString = [NSMutableString string];
    
    for(NSString *cid in self.channelIDs)
    {
        [channelIDString appendString:[NSString stringWithFormat:@"%@,",cid]]; 
    }
    
  //  channelIDString = @"2002,2006";
    
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    // YYYYMMDDhhmm
    [dateFormatter setDateFormat:@"yyyyMMddHHmm"];
    
    NSString *todayString = [dateFormatter stringFromDate:[NSDate date]];
    
    NSString *time =todayString;
    NSString *duration = @"60";
    
    NSString *urlString =[NSString stringWithFormat:@"http://epgservices.sky.com/tvlistings-proxy/TVListingsProxy/tvlistings.json?detail=2&dur=%@&time=%@&channels=%@",duration,time,channelIDString];
    
    NSLog(@"Opening URL %@",urlString);

    NSURL *url = [NSURL URLWithString:urlString];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    
    AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request 
                                         
                                                                                        success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) 
                                         {
                                             NSArray *dictionary = [JSON objectForKey:@"channels"];
                                             self.channelDetails = dictionary;
                                             
                                             [self.tableView reloadData];
                                             
                                         } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON) 
                                         {
                                             NSLog(@"Problem getting Sky channel detail %@",error);
                                         }];
    [operation start];
    
    [dateFormatter release];
}


- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [self.channelDetails count];
}

- (UITableViewCell *)tableView:(UITableView *)_tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [_tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
        
        UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, 260, 30)];
        //titleLabel.backgroundColor = [UIColor redColor];
        titleLabel.tag = 100;
        titleLabel.font = [UIFont boldSystemFontOfSize:14.0];
        [cell addSubview:titleLabel];
        [titleLabel release];
        
        UILabel *channelDescriptionLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 35, 260, 30)];
        //channelDescriptionLabel.backgroundColor =[UIColor greenColor];
        channelDescriptionLabel.tag = 101;
        channelDescriptionLabel.font =[UIFont italicSystemFontOfSize:10.0]; //[UIFont systemFontOfSize:10.0];
        [cell addSubview:channelDescriptionLabel];
        [channelDescriptionLabel release];
        
        UILabel *channelNumberLabel = [[UILabel alloc] initWithFrame:CGRectMake(270, 5, 45, 60)];
        //channelNumberLabel.backgroundColor = [UIColor yellowColor];
        channelNumberLabel.tag = 102;
        [cell addSubview:channelNumberLabel];
        [channelNumberLabel release];
     
        /*
        UIButton *btn= [UIButton buttonWithType:UIButtonTypeRoundedRect];
        btn.frame = CGRectMake(270, 5, 45, 60);
        btn.tag = 103;
        btn.backgroundColor = [UIColor redColor];
        [btn setTitle:@"Record" forState:UIControlStateNormal];
        //[btn setAccessibilityLabel:@"Record "];
        [btn addTarget:self action:@selector(buttonClick) forControlEvents:UIControlEventTouchUpInside];
        [cell addSubview:btn]; 
         */
        
        [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    }
    
    // Configure the cell...
    NSDictionary *detail = [self.channelDetails objectAtIndex:indexPath.row];
    
    id programs = [detail objectForKey:@"program"];

    
    
    //cell.textLabel.text = @"-";
    if([programs count] > 0)
    {
        NSDictionary *nowDictionary = nil;
       
        if(![programs isKindOfClass:[NSDictionary class]] && ![programs isKindOfClass:NSClassFromString(@"JKDictionary")])
        {
              nowDictionary = [programs objectAtIndex:0];
        }
        else
        {
            nowDictionary = programs;
        }
    
        if([nowDictionary isKindOfClass:NSClassFromString(@"JKDictionary")])
        {
        
            NSString *channelName = [detail objectForKey:@"title"];
            NSString *programName = [nowDictionary objectForKey:@"title"];
            
            UILabel *titleLabel = (UILabel *)[cell viewWithTag:100];
            titleLabel.text = programName;
            
            UILabel *channelDescriptionLabel = (UILabel *)[cell viewWithTag:101];
            channelDescriptionLabel.text = channelName;
            
            UILabel *channelNumberLabel = (UILabel *)[cell viewWithTag:102];
            //channelNumberLabel.text = @"1";
        }
    }
    
    return cell;
}

/*
 // Override to support conditional editing of the table view.
 - (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the specified item to be editable.
 return YES;
 }
 */

/*
 // Override to support editing the table view.
 - (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
 {
 if (editingStyle == UITableViewCellEditingStyleDelete) {
 // Delete the row from the data source
 [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
 }   
 else if (editingStyle == UITableViewCellEditingStyleInsert) {
 // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
 }   
 }
 */

/*
 // Override to support rearranging the table view.
 - (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
 {
 }
 */

/*
 // Override to support conditional rearranging of the table view.
 - (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the item to be re-orderable.
 return YES;
 }
 */

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     [detailViewController release];
     */
    
    TVGuideViewController *viewController = [[TVGuideViewController alloc] init];
    NSDictionary *detail = [self.channelDetails objectAtIndex:indexPath.row];

    id programs = [detail objectForKey:@"program"];
    

    //cell.textLabel.text = @"-";
    if([programs count] > 0)
    {
        NSDictionary *nowDictionary = nil;
        
        if(![programs isKindOfClass:[NSDictionary class]] && ![programs isKindOfClass:NSClassFromString(@"JKDictionary")])
        {
            nowDictionary = [programs objectAtIndex:0];
        }
        else
        {
            nowDictionary = programs;
        }
        
        if([nowDictionary isKindOfClass:NSClassFromString(@"JKDictionary")])
        {
            viewController.programDetail = nowDictionary;
            [self.navigationController pushViewController:viewController animated:YES];
        }
    }
    
    
    
  
    [viewController release];
    
}



@end

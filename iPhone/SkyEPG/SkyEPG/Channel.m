//
//  Channel.m
//  SkyEPG
//
//  Created by Kieran Gutteridge on 12/02/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "Channel.h"

@implementation Channel

@synthesize channelNo, epgGenre, title, channelType, channelID, genre;

-(void)dealloc
{
    self.channelNo = nil;
    self.epgGenre = nil;
    self.title = nil;
    self.channelType = nil;
    self.channelID = nil;
    self.genre =nil;
    
    [super dealloc];
}


@end

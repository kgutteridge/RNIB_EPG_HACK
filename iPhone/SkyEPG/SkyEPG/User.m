//
//  User.m
//  SkyEPG
//
//  Created by Kieran Gutteridge on 11/02/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "User.h"

@implementation User

static User *sharedUser = nil;

@synthesize username, password, token, key;


#pragma mark -
#pragma mark User Preferences
-(void)forgetUser
{
	NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
	
	[prefs removeObjectForKey:PREF_AUTOLOGIN];
	[prefs removeObjectForKey:PREF_TOKEN];
	[prefs synchronize];
	
	[self readUserPrefs];
}

-(void)readUserPrefs
{
	NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
	
	NSLog(@"Reading user prefs %@",[prefs valueForKey:PREF_TOKEN]);

	self.token = [prefs valueForKey:PREF_TOKEN];
	
}



#pragma mark Singleton Methods
+(id)sharedUser
{
	@synchronized(self)
	{
		if(sharedUser == nil)
		{
			sharedUser = [[super allocWithZone:NULL] init];
			//[sharedUser readUserPrefs];
		}
	}
	return sharedUser;
}

+(id)allocWithZone:(NSZone *)zone
{
	return [[self sharedUser] retain];
}

-(id)copyWithZone:(NSZone *)zone
{
	return self;
}

-(id)retain
{
	return self;
}

-(unsigned)retainCount
{
	return UINT_MAX;
}

-(void)release
{
	//never release
}

-(id)autorelease
{
	return self;
}

-(id)init
{
	if(self = [super init])
	{
		//set properties to defaults
	}
	return self;
}

- (NSString *)description
{
	return [NSString stringWithFormat:@"EPG User"];
	
}



@end
